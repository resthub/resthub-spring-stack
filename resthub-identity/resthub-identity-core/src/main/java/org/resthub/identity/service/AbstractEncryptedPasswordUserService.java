package org.resthub.identity.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.jasypt.util.password.PasswordEncryptor;
import org.resthub.identity.dao.UserDao;
import org.resthub.identity.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * An Abstract Implementation of a UserService dealing with OAuth2
 * authentication <br/>
 * It is based on both GenericResourceServiceImpl, userService and
 * AuthenticationService <br/>
 * Password encryption is done with JASYPT
 * 
 * It is a bean whose name is userService
 * 
 **/
@Named("userService")
public abstract class AbstractEncryptedPasswordUserService extends AbstractTraceableServiceImpl<User, UserDao> implements UserService {

    @Inject
    @Named("passwordEncryptor")
    private PasswordEncryptor passwordEncryptor;
    final static Logger logger = LoggerFactory.getLogger(AbstractEncryptedPasswordUserService.class);

    /**
     * Create a user with password encryption
     *
     * @param user
     *            the user to create
     * */
    @Transactional
    @Override
    public User create(User user) {
        if (user.getPassword() == null) {
            user.setPassword(user.generateDefaultPassword());
        }
        user.setPassword(passwordEncryptor.encryptPassword(user.getPassword()));
        return dao.save(user);
    }

    /**
     * Update a user without changing the password
     *
     * @param user
     *            the user to update
     */
    @Transactional
    @Override
    public User update(User user) {
        User userToReturn = null;
        User daoUser = dao.readByPrimaryKey(user.getId());
        if (daoUser != null) {
            user.setPassword(daoUser.getPassword());
            userToReturn = dao.save(user);
        }
        return userToReturn;
    }

    @Transactional
    @Override
    public User updatePassword(User user) {
        List<User> l = dao.findEquals("login", user.getLogin());
        int size = (l == null) ? 0 : l.size();
        User userToReturn = null;
        if (size > 0) {
            User daoUser = l.get(0);
            String newPassword = user.getPassword();
            size = (newPassword == null) ? 0 : newPassword.length();
            if (size > 0) {
                logger.debug("we are updating password of user " + daoUser.getLogin());
                daoUser.setPassword(passwordEncryptor.encryptPassword(newPassword));
                userToReturn = dao.save(daoUser);
            }
        }
        return userToReturn;
    }

    /**
     * Interesting but useless right now Only amdinb can make som modification,
     * we never do password check
     */
    @Transactional
    public User updatePassword(User user, String password) {
        List<User> l = dao.findEquals("login", user.getLogin());
        int size = (l == null) ? 0 : l.size();
        User userToReturn = null;
        if (size > 0) {
            User daoUser = l.get(0);
            Boolean passwordsMatch = passwordEncryptor.checkPassword(user.getPassword(), daoUser.getPassword());
            if (passwordsMatch) {
                if (user.getPassword().equals(password)) {
                    user.setPassword(daoUser.getPassword());
                } else {
                    user.setPassword(passwordEncryptor.encryptPassword(password));
                }
                user.setId(daoUser.getId());
                userToReturn = dao.save(user);
            }
        } else {
            user.setPassword(passwordEncryptor.encryptPassword(user.getPassword()));
            userToReturn = dao.save(user);
        }
        return userToReturn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User authenticateUser(String login, String password) {
        List<User> l = dao.findEquals("Login", login);
        if (l != null) {
            for (User tmpU : l) {
                if (passwordEncryptor.checkPassword(password, tmpU.getPassword())) {
                    return tmpU;
                }
            }
        }
        return null;
    }
}

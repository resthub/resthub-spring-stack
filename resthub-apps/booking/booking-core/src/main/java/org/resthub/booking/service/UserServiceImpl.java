package org.resthub.booking.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.booking.model.User;
import org.resthub.booking.repository.UserRepository;
import org.resthub.core.service.GenericServiceImpl;

/**
 * @author Guillaume Zurbach
 * @author bmeurant <Baptiste Meurant>
 */
@Named("userService")
public class UserServiceImpl extends GenericServiceImpl<User, Long, UserRepository> implements UserService {

    /**
     * {@inheritDoc}
     */
    @Inject
    @Named("userRepository")
    @Override
    public void setRepository(UserRepository userRepository) {
        this.repository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    public User findByUsername(String username) {
        List<User> users = this.repository.findByUsername(username);
        if (users.size() > 1) {
            throw new IllegalArgumentException("username should be unique");
        }

        if (users.isEmpty()) {
            return null;
        }

        return users.get(0);
    }

    /**
     * {@inheritDoc}
     */
    public User findByEmail(String email) {
        List<User> users = this.repository.findByEmail(email);
        if (users.size() > 1) {
            throw new IllegalArgumentException("email should be unique");
        }

        if (users.isEmpty()) {
            return null;
        }

        return users.get(0);
    }

	@Override
	public Long getIdFromEntity(User user) {
		return user.getId();
	}
}

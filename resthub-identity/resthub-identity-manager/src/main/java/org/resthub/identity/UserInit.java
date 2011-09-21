package org.resthub.identity;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.util.PostInitialize;
import org.resthub.identity.model.User;
import org.resthub.identity.service.UserService;
import org.springframework.transaction.annotation.Transactional;

@Named("userInit")
public class UserInit {

    @Inject
    @Named("userService")
    protected UserService userService;

    @PostInitialize
    @Transactional(readOnly = false)
    public void initData() {
        User u = null;

        if (userService.findByLogin("test") == null) {
            u = new User();
            u.setLogin("test");
            u.setFirstName("test");
            u.setLastName("ing");
            u.setEmail("test@resthub.org");
            u.setPassword("t3st");
            u = userService.create(u);
        }

        if (userService.findByLogin("admin") == null) {
            u = new User();
            u.setLogin("admin");
            u.setFirstName("alex");
            u.setLastName("synclar");
            u.setEmail("user1@resthub.org");
            u.setPassword("4dm|n");
            u = userService.create(u);
            userService.addPermissionToUser(u.getLogin(), "IM_USER_ADMIN");
            userService.addPermissionToUser(u.getLogin(), "IM_GROUP_ADMIN");
            userService.addPermissionToUser(u.getLogin(), "IM_ROLE_ADMIN");
            userService.addPermissionToUser(u.getLogin(), "IM_SEARCH");
        }
    }

}

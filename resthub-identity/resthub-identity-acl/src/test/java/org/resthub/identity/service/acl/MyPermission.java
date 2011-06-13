package org.resthub.identity.service.acl;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;

@SuppressWarnings("serial")
public class MyPermission extends BasePermission {

    protected MyPermission(int mask, char code) {
        super(mask, code);
    }

    public static final Permission CUSTOM = new MyPermission(1 << 5, 'X'); // 32

}

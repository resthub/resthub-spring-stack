package org.resthub.identity.service.acl.evaluator;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

/**
 * Used by Spring Security's expression-based access control implementation to
 * evaluate permissions for a particular object. Similar in behaviour to
 * {@link org.springframework.security.acls.AclEntryVoter AclEntryVoter}.
 * 
 * A null PermissionEvaluator which permit all access. Can be used to globally disable persmission checks.
 *
 * @author Tantchonta M'PO
 * @author vanackej
 */
public class PermitAllPermissionEvaluator implements PermissionEvaluator {

    private final Log logger = LogFactory.getLog(getClass());

    /**
     * @return true always
     */
    public boolean hasPermission(Authentication authentication, Object target, Object permission) {
        logger.warn("Permitting user " + authentication.getName() + " permission '" + permission + "' on object " + target);
        return true;
    }

    /**
     * @return true always
     */
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
                    Object permission) {
        logger.warn("Permitting user " + authentication.getName() + " permission '" + permission + "' on object with Id '"
                        + targetId);
        return true;
    }

}

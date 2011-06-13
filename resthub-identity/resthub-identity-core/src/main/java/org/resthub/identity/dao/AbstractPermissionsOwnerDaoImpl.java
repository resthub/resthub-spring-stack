package org.resthub.identity.dao;

import java.util.List;

import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.resthub.core.dao.GenericJpaDao;
import org.resthub.identity.model.AbstractPermissionsOwner;
import org.resthub.identity.model.Group;

/**
 * Dao for generic operations on AbstractPermissionsOwner entities.
 * 
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
@Named("abstractPermissionsOwnerDao")
public class AbstractPermissionsOwnerDaoImpl extends GenericJpaDao<AbstractPermissionsOwner, Long> implements
        AbstractPermissionsOwnerDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AbstractPermissionsOwner> getWithRoles(List<String> roles) {
        // Look for any AbstractPermissionsOwner with the defined roles
        String queryString = "select distinct e from " + AbstractPermissionsOwner.class.getName()
                + " e JOIN e.roles r where r.name IN :roles";
        TypedQuery q = this.getEntityManager().createQuery(queryString, AbstractPermissionsOwner.class);
        q.setParameter("roles", roles);
        List<AbstractPermissionsOwner> resultList = q.getResultList();
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AbstractPermissionsOwner> getWithGroupAsParent(Group group) {
        String queryString = "select distinct e from " + AbstractPermissionsOwner.class.getName()
                + " e JOIN e.groups g where g=:group";
        TypedQuery q = this.getEntityManager().createQuery(queryString, AbstractPermissionsOwner.class);
        q.setParameter("group", group);
        List<AbstractPermissionsOwner> resultList = q.getResultList();
        return resultList;
    }
}

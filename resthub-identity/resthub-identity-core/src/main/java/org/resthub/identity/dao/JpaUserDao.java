package org.resthub.identity.dao;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.resthub.core.dao.GenericJpaResourceDao;
import org.resthub.identity.model.AbstractPermissionsOwner;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.User;

/**
 * The JpaUserDao class is based on both {@link GenericJpaResourceDao} and
 * {@link UserDao}<br/>
 * 
 * It is a bean whose name is userDao
 * */
@Named("userDao")
public class JpaUserDao extends GenericJpaResourceDao<User> implements UserDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getUsersFromGroup(String groupName) {
        String queryString = "select u from " + this.getDomainClass().getName() + " u JOIN u.groups g where g.name=:groupName";
        Query q = this.getEntityManager().createQuery(queryString);
        q.setParameter("groupName", groupName);
        return q.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> findAllUsersWithRoles(List<String> roles) {
        List<User> usersWithRole = new ArrayList<User>();

        // Look for any AbstractPermissionsOwner with the defined roles
        String queryString = "select distinct e from " + AbstractPermissionsOwner.class.getName() + " e JOIN e.roles r where r.name IN :roles";
        TypedQuery q = this.getEntityManager().createQuery(queryString, AbstractPermissionsOwner.class);
        q.setParameter("roles", roles);
        List<AbstractPermissionsOwner> resultList = q.getResultList();

        // The query may have brought a mix of users and groups,
        // this loop will process them individually to form the final result.
        for (AbstractPermissionsOwner owner : resultList) {
            this.getUsersFromRootElement(usersWithRole, owner);
        }

        return usersWithRole;
    }

    /**
     * Recursive method to get all the users in an AbstractPermissionsOwner,
     * if the owner is a user, it will be directly added to the list,
     * if the owner is a group, his subgroups will be explored to find users.
     * @param users User list to add users into, must not be null.
     * @param owner Root element to begin exploration.
     */
    private void getUsersFromRootElement(List<User> users, AbstractPermissionsOwner owner) {
        // Stop the processing if one of the parameters is null
        if (users != null && owner != null) {
            // The root element may be user or a group
            if (owner instanceof User) {
                User user = (User) owner;
                // If we have a user, we can't go further so add it if needed and finish.
                if (!users.contains(user)) {
                    users.add(user);
                }
            } else if (owner instanceof Group) {
                // If we have a group, we must get both users and groups having this group as parent
                String queryString = "select distinct e from " + AbstractPermissionsOwner.class.getName() + " e JOIN e.groups g where g=:group";
                TypedQuery q = this.getEntityManager().createQuery(queryString, AbstractPermissionsOwner.class);
                q.setParameter("group", owner);
                List<AbstractPermissionsOwner> resultList = q.getResultList();

                // Each result will be recursively evaluated using this method.
                for (AbstractPermissionsOwner subOwners : resultList) {
                    this.getUsersFromRootElement(users, subOwners);
                }
            }
        }
    }
}

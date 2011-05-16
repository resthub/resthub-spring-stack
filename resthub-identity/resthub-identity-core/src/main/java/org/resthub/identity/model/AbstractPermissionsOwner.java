package org.resthub.identity.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>Describe a generic class for users and groups. It contains/manage mainly
 * permissions. When possible you SHOULD use method form the class and not from
 * the permissions list</p>
 * 
 * Must be an entity to be references by permissions_owner_permissions and 
 * permissions_owner_groups 
 */
@Entity
@Table(name = "permissions_owner")
@XmlRootElement
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractPermissionsOwner {

	private static final long serialVersionUID = 3248710006663061799L;

	private Long id;
	
	/**
	 * the list of permissions
	 * */
	protected List<String> permissions = new ArrayList<String>();

	/**
	 * List of Permissions Owner (Group) in which the Permissions Owner is
	 * */
	protected List<Group> groups = new ArrayList<Group>();
	
	/**
	 * List of roles the permission owner has.
	 */
	protected List<Role> roles = new ArrayList<Role>();
	
	/** Default constructor */
	public AbstractPermissionsOwner() {

	}

	public AbstractPermissionsOwner(AbstractPermissionsOwner i){
		List<String> pPermissions=i.getPermissions();
		permissions.addAll(pPermissions);
	}
	
	/**
	 * Constructor
	 * 
	 * @param permissions
	 *            list of permission to be assigned to the new Identity
	 * */
	public AbstractPermissionsOwner(List<String> permissions) {
		this.permissions = permissions;
	}
	
	@Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	/**
	 * Retrieve the permissions assigned to the identity
	 * 
	 * @return the permissions list if permissions have been assigned, otherwise
	 *         null
	 * */
	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "permissions_owner_permissions",
		joinColumns = @JoinColumn(name="id", referencedColumnName="id")
	)
	@XmlElementWrapper(name = "permissions")
	@XmlElement(name = "permission")
	public List<String> getPermissions() {
		return permissions;
	}

	/**
	 * <b>Only used by Hibernate</b> Please use getPermissions() instead.
	 */
	@SuppressWarnings("unused")
	private void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	
	/**
	 * Retrieve the roles assigned to the identity.
	 * 
	 * @return the list of roles assigned to the identity.
	 */
	@ManyToMany
	@JoinTable(name = "permissions_owner_roles")
	public List<Role> getRoles() {
		return this.roles;
	}
	
	/**
	 * <b>Only used by Hibernate</b> Please use getRoles() instead.
	 */
	@SuppressWarnings("unused")
	private void setRoles(List<Role> roles) {
		this.roles = roles;		
	}
	
	/**
	 * gets the user's Groups
	 * 
	 * @return the list of groups in which the user is. The List could be null
	 *         is the user is in no group
	 * 
	 */
	@ManyToMany
	@JoinTable(name = "permissions_owner_groups",
		joinColumns=
	        @JoinColumn(name="group_id", referencedColumnName="id"),
	    inverseJoinColumns=
	        @JoinColumn(name="permissions_owner", referencedColumnName="id")
	)
	public List<Group> getGroups() {
		return groups;
	}

	/**
	 * <b>Only used by Hibernate</b> Please use getGroups() instead.
	 */
	@SuppressWarnings("unused")
	private void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractPermissionsOwner other = (AbstractPermissionsOwner) obj;
       
        if ((this.id == null) ? (other.getId() != null) : !this.id.equals(other.getId())) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (this.id == null ? 0 : this.id.hashCode());
        return hash;
    }

}
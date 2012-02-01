package org.resthub.identity.repository;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.test.repository.AbstractRepositoryTest;
import org.resthub.identity.model.Role;

/**
 * Test class for <tt>RoleRepository</tt>.
 * 
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
public class RoleRepositoryTest extends AbstractRepositoryTest<Role, Long, RoleRepository> {

	private static final String ROLE_NAME = "TestRole";
	private static final String NEW_ROLE_NAME = "NewRole";

	@Inject
	@Named("roleRepository")
	@Override
	public void setRepository(RoleRepository resourceRepository) {
		super.setRepository(resourceRepository);
	}

	@Override
	protected Role createTestEntity() {
		return createTestRole();
	}

	private Role createTestRole() {
		return new Role(ROLE_NAME + Math.round(Math.random() * 100));
	}

	@Override
	public void testUpdate() {
		Role role1 = this.repository.findOne(this.id);
		role1.setName(NEW_ROLE_NAME);
		repository.save(role1);

		Role role2 = repository.findOne(this.id);
		assertEquals("Role not updated!", role2.getName(), NEW_ROLE_NAME);
	}
	
	@Override
	public Long getIdFromEntity(Role role) {
		return role.getId();
	}
}

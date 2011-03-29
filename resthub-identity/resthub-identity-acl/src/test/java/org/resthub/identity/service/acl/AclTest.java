package org.resthub.identity.service.acl;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;
import org.resthub.core.test.AbstractResthubTest;
import org.resthub.identity.model.Group;
import org.resthub.identity.service.GroupService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.FileCopyUtils;

/**
 * First unit test in order to test Spring ACL support
 * 
 *  Interesting Pointers :
 *   - Spring Security ACL reference documentation : http://static.springsource.org/spring-security/site/docs/3.0.x/reference/domain-acls.html
 *   - Spring Security ACL example
 *   	- Interface : https://fisheye.springsource.org/browse/~tag=3.0.5.RELEASE/spring-security/samples/contacts/src/main/java/sample/contact/ContactManager.java?r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=90304f64c63562ab20c24c5f014a7f55d7d883d2&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4
 *   	- Implementation : https://fisheye.springsource.org/browse/~tag=3.0.5.RELEASE/spring-security/samples/contacts/src/main/java/sample/contact/ContactManagerBackend.java?r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=90304f64c63562ab20c24c5f014a7f55d7d883d2&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4&r=052537c8b04182595e92abd1e1949b0ff7e731b4
 *   - Igenko user and permission adapters for Spring Security :
 *    	- http://code.google.com/p/igenko/source/browse/trunk/igenko-core/src/main/resources/appCtxSecurity.xml
 *    	- http://code.google.com/p/igenko/source/browse/trunk/igenko-core/src/main/java/org/igenko/core/security/IgenkoUserDetailsService.java
 *    	- http://code.google.com/p/igenko/source/browse/trunk/igenko-core/src/main/java/org/igenko/core/security/UserDetailsAdapter.java
 *    
 *   TODO :
 *    - Fix testAuthorizedDelete : it throw an AccessDeniedException, but it should'nt 
 *    - Choose between the following 2 strategies :
 *    	- Use Spring ACL to store all permissions, related to a model instance or not (I prefer this one)
 *      - Keep using permission not related to a model instance in the current model, and use Sprign ACL only for permissions related to a model instance
 *    - Update RESThub OAuth2 to support Spring Security  
 *
 */
@ContextConfiguration(locations = { "classpath*:resthubContext.xml", "classpath:resthubContext.xml", "classpath*:applicationContext.xml", "classpath:applicationContext.xml", "classpath:securityContext.xml" })
public class AclTest extends AbstractResthubTest {
	
	@Inject
	@Named("idmAclService")
	private AclService aclService;
	
	@Inject
	@Named("securedGroupService")
	private SecuredGroupService securedGroupService;
	
	@Inject
	@Named("groupService")
	private GroupService groupService;
	
	@Inject
	@Named("dataSource")
	private DriverManagerDataSource datasource;
	
	Authentication auth = new TestingAuthenticationToken("joe", "ignored");
	
	@Before
	// Create database tables if needed
	public void init() throws IOException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(datasource);
		ClassPathResource resource = new ClassPathResource("import-acl.sql");
		String sql = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
	    jdbcTemplate.execute(sql);
	    
	    // Inspired from https://fisheye.springsource.org/browse/~raw,r=052537c8b04182595e92abd1e1949b0ff7e731b4/spring-security/acl/src/test/java/org/springframework/security/acls/domain/AclImplTests.java
	    SecurityContextHolder.getContext().setAuthentication(auth);
	    Object p = auth.getPrincipal();
	    auth.setAuthenticated(true);
	    
	    this.configurePermissions();
	    
	}
	
	/** In this test we add permissions to manipulate a Group instance **/
	private void configurePermissions() {
		
		// Create Wax Taylor with no permissions on it
		Group wt = new Group();
		wt.setName("Wax Taylor");
		groupService.create(wt);
		
		// Create Hocus Pocus group, and give Joe access to it 
		Group hp = new Group();
		hp.setName("Hocus Pocus");
		hp = groupService.create(hp);

		aclService.saveAcl(hp, "joe", "CUSTOM");
	}

	@Test
	public void testAuthorizedDelete() {
	    Group g = groupService.findByName("Hocus Pocus");
		securedGroupService.delete(g);
	}
	
	@Test(expected=AccessDeniedException.class)
	public void testUnauthorizedDelete() {
		Group g = groupService.findByName("Wax Taylor");
		securedGroupService.delete(g);
	}
	 

}

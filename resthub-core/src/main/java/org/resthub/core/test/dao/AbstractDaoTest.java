package org.resthub.core.test.dao;

import javax.persistence.metamodel.SingularAttribute;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;

import org.junit.Before;
import org.junit.Test;
import org.resthub.core.dao.GenericDao;
import org.resthub.core.test.AbstractResthubTest;
import org.resthub.core.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

public abstract class AbstractDaoTest<T, PK extends Serializable, D extends GenericDao<T, PK>> extends AbstractResthubTest {

    // -----------------------------------------------------------------------------------------------------------------
    // Attributes
    /**
     * The tested DAO
     */
    protected D dao;
    /**
     * Id of the tested POJO
     */
    protected PK id;
    @PersistenceContext
    private EntityManager em;

    // -----------------------------------------------------------------------------------------------------------------
    // Property
    /**
     * Injection of DAO.
     */
    public void setDao(D dao) {
        this.dao = dao;
    } // setDao().

    // -----------------------------------------------------------------------------------------------------------------
    // Protected methods
    /**
     * Just implement this method to return the primary key from its object.
     * 
     * @param obj The object from whom we need primary key
     * @return The corresponding primary key.
     */
    protected PK getIdFromEntity(T obj) {
        EntityType<T> type = em.getMetamodel().entity((Class<T>) ClassUtils.getGenericTypeFromBean(this.dao));
        SingularAttribute<? super T, ?> attribute = type.getId(type.getIdType().getJavaType());
        return (PK) getMemberValue(attribute.getJavaMember(), obj);
    }

    /**
     * Returns the value of the given {@link Member} of the given {@link Object}.
     * . Implementation From Spring Data JPA.
     * 
     * @param member
     * @param source
     * @return
     */
    protected Object getMemberValue(Member member, Object source) {

        if (member instanceof Field) {
            Field field = (Field) member;
            ReflectionUtils.makeAccessible(field);
            return ReflectionUtils.getField(field, source);
        } else if (member instanceof Method) {
            Method method = (Method) member;
            ReflectionUtils.makeAccessible(method);
            return ReflectionUtils.invokeMethod(method, source);
        }

        throw new IllegalArgumentException(
                "Given member is neither Field nor Method!");
    }

    @SuppressWarnings("unchecked")
    protected T createTestRessource() throws Exception {
        return (T) ClassUtils.getGenericTypeFromBean(this.dao).newInstance();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setup/finalize
    @Before
    public void setUp() throws Exception {
        T resource = this.createTestRessource();
        resource = dao.save(resource);
        this.id = getIdFromEntity(resource);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Tests methods
    @Test
    public abstract void testUpdate() throws Exception;

    @Test
    public void testSave() throws Exception {
        T resource = this.createTestRessource();
        resource = dao.save(resource);

        T foundResource = dao.readByPrimaryKey(getIdFromEntity(resource));
        assertNotNull("Resource not found!", foundResource);
    }

    @Test
    public void testDelete() throws Exception {
        T resource = dao.readByPrimaryKey(this.id);
        dao.delete(resource);

        T foundResource = dao.readByPrimaryKey(this.id);
        assertNull("Resource not deleted!", foundResource);
    }

    @Test
    public void testDeleteById() throws Exception {
        dao.delete(this.id);

        T foundResource = dao.readByPrimaryKey(this.id);
        assertNull("Resource not deleted!", foundResource);
    }

    @Test
    public void testFindAll() throws Exception {
        List<T> resourceList = dao.readAll();
        assertTrue("No resources found!", resourceList.size() >= 1);
    }

    @Test
    public void testCount() throws Exception {
        Long nb = dao.count();
        assertTrue("No resources found!", nb >= 1);
    }

    @Test
    public void testReadByPrimaryKey() throws Exception {
        T foundResource = dao.readByPrimaryKey(this.id);
        assertNotNull("Resource not found!", foundResource);
        assertEquals("Resource does not contain the correct Id!", this.id, getIdFromEntity(foundResource));
    }
}

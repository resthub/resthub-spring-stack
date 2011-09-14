package org.resthub.core.test.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.resthub.core.dao.GenericDao;
import org.resthub.core.test.AbstractTransactionalTest;
import org.resthub.core.util.ClassUtils;
import org.resthub.core.util.MetamodelUtils;

/**
 * Dao test base class in order to test your DAO extending GenericDao<T, ID>
 * This can be usefull to do a quick check that everything is fine for basic
 * CRUD functionnalities.
 * 
 * Best practices are to separate these kind of automatic tests from your custom
 * tests, that may use RESThub DbUnit integration, and that should implement the
 * tests you will write.
 * 
 * @param <T>
 *            The model class managed by the generic DAO
 * @param <ID>
 *            The ID class of the model class managed by the generic DAO
 * @param <D>
 *            Your generic DAO class
 */
public abstract class AbstractDaoTest<T, ID extends Serializable, D extends GenericDao<T, ID>> extends
        AbstractTransactionalTest {

    protected static final Logger logger = Logger.getLogger(AbstractDaoTest.class);
    /**
     * The tested DAO
     */
    protected D dao;

    /**
     * Id of the tested POJO
     */
    protected ID id;

    private EntityManager em;
    
    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    /**
     * Injection of DAO.
     */
    public void setDao(D dao) {
        this.dao = dao;
    }

    /**
     * Automatically retrieve ID from entity instance.
     * 
     * @param obj
     *            The object from whom we need primary key
     * @return The corresponding primary key.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected ID getIdFromEntity(T obj) {
        MetamodelUtils utils = new MetamodelUtils<T, ID>((Class<T>) ClassUtils.getGenericTypeFromBean(this.dao),
                em.getMetamodel());
        return (ID) utils.getIdFromEntity(obj);
    }

    @SuppressWarnings("unchecked")
    protected T createTestEntity() {
        T entity;
        try {
            entity = (T) ClassUtils.getGenericTypeFromBean(this.dao).newInstance();
        } catch (Exception e) {
            logger.error("Error when creating the test entity : " + e);
            return null;
        }
        return entity;
    }

    @Before
    public void setUp() {
        T resource = this.createTestEntity();
        resource = dao.save(resource);
        this.id = getIdFromEntity(resource);
    }

    @After
    public void tearDown() {
        // Don't use deleteAll because it does not acheive cascade delete
        for (T resource : dao.readAll()) {
            dao.delete(resource);
        }
    }

    @Test
    public abstract void testUpdate();

    @Test
    public void testSave() {
        T foundResource = dao.readByPrimaryKey(this.id);
        assertNotNull("Resource not found!", foundResource);
    }

    @Test
    public void testDelete() {
        T resource = dao.readByPrimaryKey(this.id);
        dao.delete(resource);

        T foundResource = dao.readByPrimaryKey(this.id);
        assertNull("Resource not deleted!", foundResource);
    }

    @Test
    public void testDeleteById() {
        dao.delete(this.id);

        T foundResource = dao.readByPrimaryKey(this.id);
        assertNull("Resource not deleted!", foundResource);
    }

    @Test
    public void testFindAll() {
        List<T> resourceList = dao.readAll();
        assertTrue("No resources found!", resourceList.size() >= 1);
    }

    @Test
    public void testCount() {
        Long nb = dao.count();
        assertTrue("No resources found!", nb >= 1);
    }

    @Test
    public void testReadByPrimaryKey() {
        T foundResource = dao.readByPrimaryKey(this.id);
        assertNotNull("Resource not found!", foundResource);
        assertEquals("Resource does not contain the correct Id!", this.id, getIdFromEntity(foundResource));
    }
}

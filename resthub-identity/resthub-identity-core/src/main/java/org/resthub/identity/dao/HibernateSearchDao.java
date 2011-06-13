package org.resthub.identity.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.resthub.identity.model.AbstractPermissionsOwner;
import org.resthub.identity.model.Group;
import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the search DAO with HibernateSearch
 */
@Named("searchDao")
public class HibernateSearchDao implements SearchDao {

    /**
     * Classe's logger
     */
    protected final static Logger logger = LoggerFactory.getLogger(HibernateSearchDao.class);

    /**
     * JPA persistence context, injected by Spring.
     */
    @PersistenceContext
    protected EntityManager entityManager;

    /**
     * Inihibition flag. No query should be realized while re-indexing
     * resources.
     * http://docs.jboss.org/hibernate/search/3.4/reference/en-US/html
     * /manual-index-changes.html#search-batchindex-massindexer
     */
    protected boolean inhibitSearch = false;

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<AbstractPermissionsOwner> search(String queryString, boolean withUsers, boolean withGroups) {
        List<AbstractPermissionsOwner> results = new ArrayList<AbstractPermissionsOwner>();
        // No query should be realized while re-indexing resources.
        if (!inhibitSearch) {
            // Gets the Hibernate search object to performs queries.
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

            // Parse the the queryString.
            MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_30, new String[] { "name",
                    "firstName", "lastName", "email", "login" }, new StandardAnalyzer(Version.LUCENE_31));
            parser.setDefaultOperator(Operator.OR);

            try {
                Query luceneQuery = parser.parse(queryString);

                FullTextQuery query = null;
                // Because of the poor design of the Hibernate Search API and
                // the usage of varagrs, we must have this
                // if-else algorihm. TODO refactor with reflection.
                if (withUsers && withGroups) {
                    query = fullTextEntityManager.createFullTextQuery(luceneQuery, User.class, Group.class);
                } else if (withUsers) {
                    query = fullTextEntityManager.createFullTextQuery(luceneQuery, User.class);
                } else if (withGroups) {
                    query = fullTextEntityManager.createFullTextQuery(luceneQuery, Group.class);
                }
                // Finally execute the query.
                if (query != null) {
                    List<AbstractPermissionsOwner> found = query.getResultList();
                    // Keeps only distinct results.
                    for (AbstractPermissionsOwner foundObject : found) {
                        if (!results.contains(foundObject)) {
                            // TODO Remove this Hibernate specific block.
                            // Sometimes hibernate Search returns Javassist
                            // proxies, which can't be properly
                            // deserialize by Jackson.
                            if (foundObject instanceof HibernateProxy) {
                                HibernateProxy h = (HibernateProxy) foundObject;
                                foundObject = (AbstractPermissionsOwner) h.getHibernateLazyInitializer()
                                        .getImplementation();
                            }
                            results.add(foundObject);
                        }
                    }
                }
            } catch (ParseException exc) {
                // Handle parsing failure
                String error = "Misformatted queryString '" + queryString + "': " + exc.getMessage();
                logger.debug("[search] " + error);
                throw new IllegalArgumentException(error, exc);
            }
        }
        return results;
    } // search().

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetIndexes() {
        logger.info("[resetIndexes] Re-indexing all users, groups and roles...");
        this.inhibitSearch = true;
        try {
            FullTextEntityManager searchFactory = Search.getFullTextEntityManager(entityManager);
            searchFactory.createIndexer(User.class, Group.class, Role.class).startAndWait();
        } catch (InterruptedException exc) {
            logger.error("[reset] Fatal error while re-indexing users, groups and roles " + exc.getMessage(), exc);
        }
        // Remove inhibition flag
        this.inhibitSearch = false;
        logger.info("[resetIndexes] Re-indexation finished");
    } // resetIndexes().

} // interface SearchDao

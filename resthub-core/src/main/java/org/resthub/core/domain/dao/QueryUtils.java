/*
 * Copyright 2008-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.resthub.core.domain.dao;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.resthub.core.domain.Sort;
import org.resthub.core.domain.Sort.Property;
import org.resthub.core.util.ClassUtils;
import org.springframework.util.Assert;


/**
 * Simple utility class to create JPA queries.
 * 
 * @author Oliver Gierke - gierke@synyx.de
 */
public abstract class QueryUtils {

    public static final String COUNT_QUERY_STRING = "select count(x) from %s x";

    public static final String DELETE_ALL_QUERY_STRING = "delete from %s x";
    public static final String READ_ALL_QUERY = "select x from %s x";
    private static final String DEFAULT_ALIAS = "x";

    private static final String COUNT_MATCH = "(?<=select )(.*)(?= from)";
    private static final String COUNT_REPLACEMENT = "count(*)";

    private static final Pattern ALIAS_MATCH;

    static {

        StringBuilder builder = new StringBuilder();
        builder.append("(?<=from)"); // from as starting delimiter
        builder.append("(?: )+"); // at least one space separating
        builder.append("([\\w.]*)"); // Entity name, can be qualified
        builder.append("(?: as)*"); // exclude possible "as" keyword
        builder.append("(?: )+"); // at least one space separating
        builder.append("(\\w*)"); // the actual alias

        ALIAS_MATCH = compile(builder.toString(), CASE_INSENSITIVE);
    }


    /**
     * Private constructor to prevent instantiation.
     */
    private QueryUtils() {

    }


    /**
     * Returns the query string for the given class.
     * 
     * @return
     */
    public static String getQueryString(String template, Class<?> clazz) {

        if (null == clazz) {
            throw new IllegalArgumentException("Class must not be null!");
        }

        return getQueryString(template, ClassUtils.getEntityName(clazz));
    }


    /**
     * Returns the query string for the given class name.
     * 
     * @param template
     * @param clazzName
     * @return
     */
    public static String getQueryString(String template, String clazzName) {

        Assert.hasText(clazzName, "Classname must not be null or empty!");

        return String.format(template, clazzName);
    }


    /**
     * Adds {@literal order by} clause to the JPQL query. Uses the
     * {@link #DEFAULT_ALIAS} to bind the sorting property to.
     * 
     * @param query
     * @param alias
     * @param sort
     * @return
     */
    public static String applySorting(String query, Sort sort) {

        return applySorting(query, sort, DEFAULT_ALIAS);
    }


    /**
     * Adds {@literal order by} clause to the JPQL query.
     * 
     * @param query
     * @param sort
     * @param alias
     * @return
     */
    public static String applySorting(String query, Sort sort, String alias) {

        if (null == sort) {
            return query;
        }

        Assert.hasText(alias);

        StringBuilder builder = new StringBuilder(query);
        builder.append(" order by");

        for (Property property : sort) {
            builder.append(String.format(" %s.", alias));
            builder.append(property.getName());
            builder.append(" ");
            builder.append(property.getOrder().getJpaValue());
            builder.append(",");
        }

        builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }


    /**
     * Resolves the alias for the entity to be retrieved from the given JPA
     * query.
     * 
     * @param query
     * @return
     */
    public static String detectAlias(String query) {

        Matcher matcher = ALIAS_MATCH.matcher(query);

        return matcher.find() ? matcher.group(2) : null;
    }


    /**
     * Creates a where-clause referencing the given entities and appends it to
     * the given query string. Binds the given entities to the query.
     * 
     * @param <T>
     * @param queryString
     * @param entities
     * @param entityManager
     * @return
     */
    public static <T> Query applyAndBind(String queryString,
            Collection<T> entities, EntityManager entityManager) {

        Assert.notNull(queryString);
        Assert.notNull(entities);
        Assert.notNull(entityManager);

        Iterator<T> iterator = entities.iterator();

        if (!iterator.hasNext()) {
            return entityManager.createQuery(queryString);
        }

        String alias = detectAlias(queryString);
        StringBuilder builder = new StringBuilder(queryString);
        builder.append(" where");

        for (int i = 0; i < entities.size(); i++) {

            builder.append(String.format(" %s = ?", alias));

            if (i < entities.size() - 1) {
                builder.append(" or");
            }
        }

        Query query = entityManager.createQuery(builder.toString());

        for (int i = 0; i < entities.size(); i++) {
            query.setParameter(i + 1, iterator.next());
        }

        return query;
    }


    /**
     * Creates a count projected query from the given orginal query.
     * 
     * @param originalQuery must not be {@literal null} or empty
     * @return
     */
    public static String createCountQueryFor(String originalQuery) {

        Assert.hasText(originalQuery);

        if (originalQuery.startsWith("from")) {
            return String.format("select %s %s", COUNT_REPLACEMENT,
                    originalQuery);
        }

        return originalQuery.replaceFirst(COUNT_MATCH, COUNT_REPLACEMENT);
    }
}

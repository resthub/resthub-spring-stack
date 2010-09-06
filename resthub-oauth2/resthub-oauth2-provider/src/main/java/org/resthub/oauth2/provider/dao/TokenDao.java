package org.resthub.oauth2.provider.dao;

import org.resthub.core.dao.GenericDao;
import org.resthub.oauth2.common.model.Token;

/**
 * DAO to manipulates tokens in database.
 */
public interface TokenDao extends GenericDao<Token, Long> {

} // interface TokenDao.

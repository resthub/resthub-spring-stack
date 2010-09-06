package org.resthub.oauth2.provider.dao;

import javax.inject.Named;
import javax.inject.Singleton;

import org.resthub.core.dao.GenericJpaDao;
import org.resthub.oauth2.common.model.Token;

/**
 * Implementation of the token's dao
 */
@Named("tokenDao")
@Singleton
public class TokenDaoImpl extends GenericJpaDao<Token, Long> implements TokenDao {

} // class TockenDaoImpl

package org.resthub.oauth2.provider.dao;

import javax.inject.Named;
import javax.inject.Singleton;

import org.resthub.core.dao.GenericJpaResourceDao;
import org.resthub.oauth2.provider.model.Token;

/**
 * Implementation of the token's dao
 */
@Named("tokenDao")
@Singleton
public class TokenDaoImpl extends GenericJpaResourceDao<Token> implements TokenDao {

} // class TockenDaoImpl

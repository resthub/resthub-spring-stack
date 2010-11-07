#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.dao;

import org.resthub.core.dao.GenericResourceDao;
import ${package}.model.SampleResource;

public interface SampleDao extends GenericResourceDao<SampleResource>  {

}

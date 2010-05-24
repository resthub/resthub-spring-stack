#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.dao;

import javax.inject.Named;

import org.resthub.core.dao.GenericJpaResourceDao;
import ${package}.model.SampleResource;

@Named("sampleDao")
public class JpaSampleDao extends GenericJpaResourceDao<SampleResource> implements SampleDao {

}

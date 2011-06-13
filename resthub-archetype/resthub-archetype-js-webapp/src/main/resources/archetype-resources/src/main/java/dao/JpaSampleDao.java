#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.dao;

import javax.inject.Named;

import org.resthub.core.dao.GenericJpaDao;
import ${package}.dao.SampleDao;
import ${package}.model.Sample;

@Named("sampleDao")
public class JpaSampleDao extends GenericJpaDao<Sample, Long> implements SampleDao {

}

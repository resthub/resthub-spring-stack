#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.dao;

import org.resthub.core.dao.GenericDao;
import ${package}.model.Sample;

public interface SampleDao extends GenericDao<Sample, Long>  {

}

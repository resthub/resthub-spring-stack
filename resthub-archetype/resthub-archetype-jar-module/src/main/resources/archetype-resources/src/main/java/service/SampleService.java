#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import org.resthub.common.service.GenericService;
import ${package}.model.Sample;

public interface SampleService extends GenericService<Sample, Long> {

}

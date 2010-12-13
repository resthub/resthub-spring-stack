#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import org.resthub.core.service.GenericResourceService;
import ${package}.model.SampleResource;

public interface SampleService extends GenericResourceService<SampleResource> {

}

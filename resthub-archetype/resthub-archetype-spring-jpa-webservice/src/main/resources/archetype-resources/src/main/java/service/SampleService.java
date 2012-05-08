#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import org.resthub.common.service.RestService;

import ${package}.model.Sample;

public interface SampleService extends RestService<Sample, Long> {

}

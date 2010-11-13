#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.webapp.t5.pages;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;

import ${package}.service.SampleService;
import ${package}.model.SampleResource;

public class Index {

	@Inject
	@Service("sampleService")
	private SampleService sampleService;
	
	@Property
	private SampleResource sampleResource;
	
	void setupRender() {
		this.sampleResource = sampleService.findById(1L);
	}
}

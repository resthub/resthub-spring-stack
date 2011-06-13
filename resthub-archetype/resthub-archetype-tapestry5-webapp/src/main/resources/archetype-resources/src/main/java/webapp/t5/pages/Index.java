#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.webapp.t5.pages;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;

import ${package}.service.SampleService;
import ${package}.model.Sample;

public class Index {

	@Inject
	@Service("sampleService")
	private SampleService sampleService;
	
	@Property
	private Sample sample;
	
	void setupRender() {
		this.sample = sampleService.findById(1L);
	}
}

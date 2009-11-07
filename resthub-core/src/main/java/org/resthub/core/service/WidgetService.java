package org.resthub.core.service;

import java.io.IOException;

import org.resthub.core.domain.model.Widget;
import org.resthub.core.exception.WidgetCompilationException;

/**
 * Add compilation capabilities to ResourceService for technologies like Flex 
 * that use compiled widgets.
 * 
 * @author bouiaw
 */
public interface WidgetService extends ResourceService {
	
	/**
	 * Compile a widget
	 * @param store define if the compiled widget is stored in the JCR
	 * @return the name of the compiled file
	 */
	public String compile(Widget widget) throws IOException, WidgetCompilationException;
	public String compile(Widget widget, boolean store) throws IOException, WidgetCompilationException;

}

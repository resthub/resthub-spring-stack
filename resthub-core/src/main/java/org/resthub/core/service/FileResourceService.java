package org.resthub.core.service;

import java.io.InputStream;

/**
 * Allow associating a binary Stream to a FileResource
 * 
 * @author pastis
 */
public interface FileResourceService extends ResourceService {

	/**
	 * Upload the resource on the given path by set
	 * the value of the given file stream
	 * 
	 * @param path : path of the resource to update
	 * @param fileName : name of the uploaded file
	 * @param stream : stream to set as new value for the given attribute
	 */
	void updateFileResource(String path, String fileName, InputStream stream);
	
}

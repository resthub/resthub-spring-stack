package org.resthub.core.service;


/**
 * Provide utilities for File Management
 * 
 * @author pastis
 */
public interface FileService {

	/**
	 * Gives the correct mime type regarding the extension contained into
	 * the file name
	 * 
	 * @param fileName: file name
	 * 
	 * @return The correct MimeType 
	 */
	String getMimeTypeFromFileName(String fileName);

}

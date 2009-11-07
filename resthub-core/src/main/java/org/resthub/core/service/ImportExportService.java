package org.resthub.core.service;

import java.io.InputStream;
import java.io.OutputStream;

import org.resthub.core.exception.ResthubException;

/**
 * Import and export contents from the Java Content Repository
 * 
 * @author bouiaw
 */
public interface ImportExportService {
	
	/**
	 * Import resources from an xml file to the root 
	 */
	public void exportResources(OutputStream outputStream) throws ResthubException;
	
	/**
	 * Export all the repository to an output stream 
	 */
	public void importResources(InputStream inputStream) throws ResthubException;
	
	
}

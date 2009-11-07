package org.resthub.core.domain.model;

import org.jcrom.JcrFile;
import org.jcrom.annotations.JcrFileNode;

/**
 * Resource intended to store a file in the Java Content Repository.
 * 
 * @author bouiaw
 */
public class FileResource extends Resource {

	private static final long serialVersionUID = -747177309812059850L;
	
	/**
	 * File that contains the compiled output of the code
	 * Used for example to store the compiled SWF file of a Flex widget
	 */
	@JcrFileNode
	JcrFile file;
	
	/**
	 * @return the associated file
	 */
	public JcrFile getFile() {
		return file;
	}

	/**
	 * Change the associated file
	 * 
	 * @param file : new file
	 */
	public void setFile(JcrFile file) {
		this.file = file;
	}

	/**
	 * @return the current file name or null if there is no
	 * associated file
	 */
	public String getFileName() {
		if (null != this.file) {
			return this.file.getName();
		}
		else {
			return null;
		}
	}
	
	/**
	 * @return the current file mime type or null if there is no
	 * associated file
	 */
	public String getMimeType() {
		if (null != this.file) {
			return this.file.getMimeType();
		}
		else {
			return null;
		}
	}

}

package org.resthub.core.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.resthub.core.domain.model.FileResource;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.exception.ResthubException;
import org.resthub.core.service.FileResourceService;
import org.resthub.core.service.FileService;
import org.jcrom.JcrFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation for FileResource Management
 * 
 * @author pastis
 */
@Service("fileResourceService" )
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
public class FileResourceServiceImpl extends ResourceServiceImpl implements FileResourceService {
	
	private static Logger logger = LoggerFactory.getLogger(FileResourceServiceImpl.class);
	
	/**
	 * Service providing file utilities
	 */
	private FileService fileService;
	
	
	/**
	 * Injection of FileService implementation
	 * 
	 * @param fileService : FileService implementation
	 */
	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}



	/**
	 * {@inheritDoc} 
	 */
	@Transactional(readOnly = false)
	public void updateFileResource(String path, String fileName, InputStream stream) {
		
		try {
			FileResource fileResource = (FileResource)retreive(path);
			
			File f = new File(fileName);
			OutputStream out = new BufferedOutputStream(new FileOutputStream(f));
			
			byte[] buffer = new byte[10 * 1024];
			int numRead;
			long numWritten = 0;
			while ((numRead = stream.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
			}

			JcrFile jcrFile = JcrFile.fromFile(fileName, f, fileService.getMimeTypeFromFileName(fileName));
			
//			JcrFile jcrFile = fileResource.getFile();
//			jcrFile.setName(fileName);
//			jcrFile.setMimeType(fileService.getMimeTypeFromFileName(fileName));
//			jcrFile.setLastModified(Calendar.getInstance());
//			jcrFile.setDataProvider( new JcrDataProviderImpl(JcrDataProvider.TYPE.STREAM, stream) );
			
			fileResource.setFile(jcrFile);
			super.update(fileResource);
			
		}
		catch (ClassCastException e) {
			logger.error("Bad Path "+path+". Path should reference a FileResource instance");
		}
		catch (IOException e) {
			logger.error("Error reading input stream");
		}
	}
		
	@Transactional(readOnly = false)
	@Override
	public Resource update(Resource resource) throws ResthubException {
		if (null == resource) {
			throw new IllegalArgumentException("resource cannot be null !!");
		}
		if (!(resource instanceof FileResource)) {
			throw new IllegalArgumentException("resource must be a FileResource instance!!");
		}
//		FileResource fileResource = (FileResource)retreive(resource.getPath());
//		((FileResource)resource).setFile(fileResource.getFile()); 
		return super.update(resource);
	}
	

	
}

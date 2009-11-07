package org.resthub.core.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.resthub.core.domain.dao.ResourceDao;
import org.resthub.core.exception.ResthubException;
import org.resthub.core.exception.ImportExportResourceException;
import org.resthub.core.service.ImportExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("importExportService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ImportExportServiceImpl implements ImportExportService {
	
	private static Logger logger = LoggerFactory.getLogger(ImportExportServiceImpl.class);

	@Autowired
	private ResourceDao resourceDao;
	
	private List<String> pathToExport;

	public ResourceDao getResourceDao() {
		return resourceDao;
	}

	public void setResourceDao(ResourceDao resourceDao) {
		this.resourceDao = resourceDao;
	}

	public List<String> getPathToExport() {
		return pathToExport;
	}

	public void setPathToExport(List<String> pathToExport) {
		this.pathToExport = pathToExport;
	}

    /**
	 * {@inheritDoc}
	 */
	public void exportResources(OutputStream outputStream) throws ResthubException {
		 ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream); 
		 zipOutputStream.setMethod(ZipOutputStream.DEFLATED); 
		 zipOutputStream.setLevel(Deflater.BEST_COMPRESSION);
		 
		 try {
			 for(String path : pathToExport) {
				 
				 if(resourceDao.exists(path)) {
					 // Change / to - in order to have a valid entry name;
					 String entryName = path.substring(1, path.length() - 1).replace("/", "-") + ".xml";
					 
					 ZipEntry zipEntry = new ZipEntry(entryName);
					 zipOutputStream.putNextEntry(zipEntry);
					 resourceDao.exportResources(path, zipOutputStream);
					 zipOutputStream.closeEntry();
				 }
				 
			 }
			 zipOutputStream.close();
		 }
		 catch (IOException e) {
			 throw new ImportExportResourceException (e.getMessage(), e);
		 }
	}

    /**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false)
	public void importResources(InputStream inputStream) throws ResthubException {
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		
		try  {
			ZipEntry zipEntry = zipInputStream.getNextEntry();
			
			while (zipEntry != null) {
				String name = zipEntry.getName();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
	
				for(int c = zipInputStream.read();c != -1; c=zipInputStream.read()) out.write(c);
				logger.info("Import " + name);
				resourceDao.importResources("/", new ByteArrayInputStream(out.toByteArray()));
				zipEntry = zipInputStream.getNextEntry();
			}
		}
		 catch (IOException e) {
			 throw new ImportExportResourceException (e.getMessage(), e);
		 }
	}
}

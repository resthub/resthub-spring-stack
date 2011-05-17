package org.resthub.roundtable.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * Illustration controller.
 * @author Nicolas Carlier
 */
@Path("/illustration")
@Named("illustrationController")
@Singleton
public class IllustrationControler {
    private static final Logger logger = LoggerFactory.getLogger(IllustrationControler.class);
    
    @Value("#{config['rt.data.dir']}")
    private String dataDirPath;

    class UploadResponse {
	private List<String> refs = new ArrayList<String>();

	protected void addUploadRef(String ref) {
	    refs.add(ref);
	}

	protected String build() {
	    StringBuilder response = new StringBuilder("{\"success\" : \"true\", \"files\" : [");
	    for (String ref : refs) {
		response.append("\"").append(ref).append("\", ");
	    }
	    response.append("]}");
	    return response.toString();
	}
    }
    
    @PostConstruct
	protected void init() {
		String illustrationLocation = new StringBuilder(this.dataDirPath).append(File.separator).append("illustration").toString();
		logger.debug("Illustration location : " + illustrationLocation);
		File dataDir = new File(illustrationLocation);
		if(!dataDir.exists())
			dataDir.mkdirs();
	}

    @POST
    @Path("/upload")
    @Produces(MediaType.TEXT_HTML)
    public Response upload(@Context HttpServletRequest request) {
	UploadResponse response = new UploadResponse();
	if (ServletFileUpload.isMultipartContent(request)) {
	    FileItemFactory factory = new DiskFileItemFactory();
	    ServletFileUpload upload = new ServletFileUpload(factory);
	    List<FileItem> items = null;
	    try {
		items = upload.parseRequest(request);
	    } catch (FileUploadException ex) {
		return Response.serverError().header("FileUploadException", ex.getMessage()).build();
	    }
	    if (items != null) {
		Iterator<FileItem> iter = items.iterator();
		while (iter.hasNext()) {
		    FileItem item = iter.next();
		    if (!item.isFormField() && item.getSize() > 0) {
			String fileName = processFileName(item.getName());
			logger.debug("Uploading file : {} ...", fileName);
			File targetFile = null;
			try {
			    targetFile = File.createTempFile("rt_", ".attachement");
			    targetFile.deleteOnExit();
			    item.write(targetFile);
			} catch (Exception ex) {
			    return Response.serverError().header("FileUploadException", ex.getMessage()).build();
			}
			response.addUploadRef(getFileRef(targetFile.getAbsolutePath()));
			logger.debug("File {} uploaded ({}).", fileName, targetFile.getAbsolutePath());
		    }
		}
	    }
	}
	return Response.ok(response.build()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getIllustration(@PathParam("id") String id){
        String illustrationLocation = new StringBuilder(this.dataDirPath)
                .append(File.separator).append("illustration")
                .append(File.separator).append(id).toString();
       
        File file = new File(illustrationLocation);
        if (!file.exists()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
	
	String mt = new MimetypesFileTypeMap().getContentType(file);
	return Response.ok(file, mt).build();
    }

    private static String processFileName(String fileNameInput) {
	String fileNameOutput = null;
	fileNameOutput = fileNameInput.substring(
		fileNameInput.lastIndexOf("\\") + 1, fileNameInput.length());
	return fileNameOutput;
    }

    private static String getFileRef(String fileName) {
	Pattern p = Pattern.compile("rt_([0-9]+)\\.attachement$");
	Matcher m = p.matcher(fileName);
	return (m.find()) ? m.group(1) : fileName;
    }

}

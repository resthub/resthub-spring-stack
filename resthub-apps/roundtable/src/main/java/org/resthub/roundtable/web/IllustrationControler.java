package org.resthub.roundtable.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;

import org.resthub.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

/**
 * Illustration controller.
 * 
 * @author Nicolas Carlier
 */
@Controller @RequestMapping("/api/illustration")
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
        String illustrationLocation = new StringBuilder(this.dataDirPath).append(File.separator).append("illustration")
                .toString();
        logger.debug("Illustration location : " + illustrationLocation);
        File dataDir = new File(illustrationLocation);
        if (!dataDir.exists())
            dataDir.mkdirs();
    }


    @RequestMapping(method = RequestMethod.POST, value = "upload") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void upload(@RequestParam("file") MultipartFile file) throws IOException {
        
    	InputStream in = file.getInputStream();
    	
//    	UploadResponse response = new UploadResponse();
//        if (ServletFileUpload.isMultipartContent(request)) {
//            FileItemFactory factory = new DiskFileItemFactory();
//            ServletFileUpload upload = new ServletFileUpload(factory);
//            List<FileItem> items = null;
//           
//                items = upload.parseRequest(request);
//            
//            if (items != null) {
//                Iterator<FileItem> iter = items.iterator();
//                while (iter.hasNext()) {
//                    FileItem item = iter.next();
//                    if (!item.isFormField() && item.getSize() > 0) {
//                        String fileName = processFileName(item.getName());
//                        logger.debug("Uploading file : {} ...", fileName);
//                        File targetFile = null;
//                        try {
//                            targetFile = File.createTempFile("rt_", ".attachement");
//                            targetFile.deleteOnExit();
//                            item.write(targetFile);
//                        } catch (Exception ex) {
//                            return Response.serverError().header("FileUploadException", ex.getMessage()).build();
//                        }
//                        response.addUploadRef(getFileRef(targetFile.getAbsolutePath()));
//                        logger.debug("File {} uploaded ({}).", fileName, targetFile.getAbsolutePath());
//                    }
//                }
//            }
//        }
//        return Response.ok(response.build()).build();
    }


    
    @RequestMapping(method = RequestMethod.GET, value = "{id}") @ResponseBody
    public ResponseEntity<File> getIllustration(@PathVariable("id") String id) {
        String illustrationLocation = new StringBuilder(this.dataDirPath).append(File.separator).append("illustration")
                .append(File.separator).append(id).toString();

        File file = new File(illustrationLocation);
        if (!file.exists()) {
            throw new NotFoundException();
        }

        String mt = new MimetypesFileTypeMap().getContentType(file);
        return new ResponseEntity<File>(file, HttpStatus.OK);
    }

    private static String processFileName(String fileNameInput) {
        String fileNameOutput = null;
        fileNameOutput = fileNameInput.substring(fileNameInput.lastIndexOf("\\") + 1, fileNameInput.length());
        return fileNameOutput;
    }

    private static String getFileRef(String fileName) {
        Pattern p = Pattern.compile("rt_([0-9]+)\\.attachement$");
        Matcher m = p.matcher(fileName);
        return (m.find()) ? m.group(1) : fileName;
    }

}

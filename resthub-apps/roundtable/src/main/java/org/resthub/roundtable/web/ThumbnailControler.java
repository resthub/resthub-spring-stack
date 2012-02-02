package org.resthub.roundtable.web;

import java.io.File;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;

import org.resthub.roundtable.toolkit.ImageTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Thumbnail controller.
 * 
 * @author Nicolas Carlier
 */
@Controller @RequestMapping("/api/thumbnail")
public class ThumbnailControler {
    private static final Logger logger = LoggerFactory.getLogger(ThumbnailControler.class);

    @Value("#{config['rt.data.dir']}")
    private String dataDirPath;

    @PostConstruct
    protected void init() {
        String thumbnailLocation = new StringBuilder(this.dataDirPath).append(File.separator).append("thumbnail")
                .toString();
        logger.debug("Thumbnail location : " + thumbnailLocation);
        File dataDir = new File(thumbnailLocation);
        if (!dataDir.exists())
            dataDir.mkdirs();
    }

    
    @RequestMapping(method=RequestMethod.GET, value= "{id}")
    public ResponseEntity<File> getThumbnail(@RequestParam("id") String id, @RequestParam(value="tmp", required=false) Boolean tmp) {

    	tmp = (tmp== null) ? false : tmp;
    	
        String thumbnailDir = new StringBuilder(dataDirPath).append(File.separator).append("thumbnail").toString();
        String thumbnailLocation = new StringBuilder(thumbnailDir).append(File.separator).append(id).toString();

        File file = new File(thumbnailLocation);
        if (!file.exists()) {
            String illustrationLocation;
            if (Boolean.TRUE.equals(tmp)) {
                String tmpdir = System.getProperty("java.io.tmpdir");
                illustrationLocation = new StringBuilder(tmpdir).append(File.separator).append("rt_").append(id)
                        .append(".attachement").toString();
            } else {
                illustrationLocation = new StringBuilder(dataDirPath).append(File.separator).append("illustration")
                        .append(File.separator).append(id).toString();
            }
           
            try {
				ImageTools.createThumbnail(illustrationLocation, thumbnailLocation, 100);
			} catch (IOException | InterruptedException e) {
				return new ResponseEntity<File>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
            
            file = new File(thumbnailLocation);
            
            
        
        }

        String mt = new MimetypesFileTypeMap().getContentType(file);
        HttpHeaders responseHeaders = new HttpHeaders();
        
        responseHeaders.setContentType(MediaType.parseMediaTypes(mt).get(0));
        
        return new ResponseEntity<File>(file, responseHeaders, HttpStatus.CREATED);

    }

}

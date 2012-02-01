package org.resthub.roundtable.web;

import java.io.File;
import java.io.IOException;
import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.resthub.roundtable.toolkit.ImageTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getThumbnail(@PathParam("id") String id, @QueryParam("tmp") @DefaultValue("false") Boolean tmp) {

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
                file = new File(thumbnailLocation);
            } catch (IOException ex) {
                return Response.serverError().header("Unable to create thumbnail", ex.getMessage()).build();
            } catch (InterruptedException ex) {
                return Response.serverError().header("Unable to create thumbnail", ex.getMessage()).build();
            }
        }

        String mt = new MimetypesFileTypeMap().getContentType(file);
        return Response.ok(file, mt).build();
    }

}

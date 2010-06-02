package org.resthub.roundtable.web.controller;

import com.jamonapi.MonitorFactory;
import com.sun.jersey.api.view.ImplicitProduces;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Monitoring controller.
 * @author Nicolas Carlier
 */
@Path("/monitoring")
@Named("monitoringController")
@Singleton
@ImplicitProduces("text/html;qs=5")
public class MonitoringController {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getMonitoringReport() {
        return Response.ok(MonitorFactory.getReport()).build();
    }
}

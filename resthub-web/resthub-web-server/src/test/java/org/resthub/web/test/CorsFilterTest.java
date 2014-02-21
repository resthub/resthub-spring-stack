package org.resthub.web.test;

import com.thetransactioncompany.cors.CORSFilter;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.fest.assertions.api.Assertions;
import org.resthub.test.AbstractWebTest;
import org.resthub.web.Http;
import org.resthub.web.Response;
import org.resthub.web.model.Sample;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import java.util.EnumSet;

import static org.fest.assertions.api.Assertions.assertThat;

public class CorsFilterTest extends AbstractWebTest {

    public CorsFilterTest() {
        super("resthub-web-server, resthub-jpa, resthub-pool-bonecp", 9798);
        this.startServerOnce=false;
    }

    @Override
    protected ServletContextHandler customizeContextHandler(ServletContextHandler context) throws ServletException {

        /*
         See http://software.dzhuvinov.com/cors-filter-configuration.html
         for additional CORSFilter init parameters
         */
        CORSFilter corsFilter = new CORSFilter();
        FilterHolder holder = new FilterHolder(corsFilter);
        // some HTTP methods are not allowed by this filter by default
        holder.setInitParameter("cors.supportedMethods", "GET, POST, PUT, DELETE, HEAD, OPTIONS");
        context.addFilter(holder, "/*", EnumSet.of(DispatcherType.REQUEST));

        return context;
    }

    @Test
    public void testCORSOriginHeader() {
        Sample r = new Sample("toto");
        r = this.request("service-based").jsonPost(r).resource(r.getClass());
        Response response = this.request("service-based/" + r.getId()).setHeader("Origin", "http://example.org").get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.OK);
        assertThat(response.getHeader("Access-Control-Allow-Origin")).isNotNull().isEqualTo("http://example.org");
    }

    @AfterMethod
    public void tearDown() {
        this.request("service-based").delete();
    }

}

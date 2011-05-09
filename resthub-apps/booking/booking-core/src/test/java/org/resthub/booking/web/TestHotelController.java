package org.resthub.booking.web;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.resthub.booking.model.Hotel;
import org.resthub.web.test.AbstractWebResthubTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.WebResource;

public class TestHotelController extends AbstractWebResthubTest {

    private static final Logger LOG = LoggerFactory
    .getLogger(TestHotelController.class);

	@Test
    @Ignore
    public void testCreateHotel() {
		WebResource r = resource().path("hotel");
		Hotel hotel = new Hotel();
		hotel.setName("Westin Diplomat");
		hotel.setAddress("3555 S. Ocean Drive");
		hotel.setZip("33019");
		hotel.setCity("Hollywood");
		hotel.setState("FL");
		hotel.setCountry("USA");

		r.type(MediaType.APPLICATION_XML).post(String.class, hotel);
    }

	@Test
	@Ignore
    public void testFindAllHotels() {

		WebResource r = resource().path("hotel");
		
        String response = r.type(MediaType.APPLICATION_XML).get(String.class);
		LOG.info(response + "\n");
		
        Assert.assertTrue(response.contains("<pageResponse>"));
        Assert.assertTrue(response.contains("<elements>"));
    }

    @Test
    @Ignore
    public void testSearchHotels() {
        WebResource r = resource().path("hotel/search").queryParam("q", "Westin");
        String response = r.accept(MediaType.APPLICATION_XML).get(String.class);
        LOG.info(response + "\n");
        Assert.assertTrue(response.contains("Westin"));
    }

}

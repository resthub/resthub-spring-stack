package org.resthub.booking.web;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;
import org.junit.Test;
import org.resthub.web.test.AbstractWebResthubTest;
import com.sun.jersey.api.client.WebResource;
import org.resthub.booking.model.Hotel;

public class TestHotelController extends AbstractWebResthubTest {


	@Test
    public void testCreateHotel() {
		/*WebResource r = resource().path("hotel");
		Hotel hotel = new Hotel();
		hotel.setName("Westin Diplomat");
		hotel.setAddress("3555 S. Ocean Drive");
		hotel.setZip("33019");
		hotel.setCity("Hollywood");
		hotel.setState("FL");
		hotel.setCountry("USA");

		r.type(MediaType.APPLICATION_XML).post(String.class, hotel);*/
    }

	@Test
    public void testFindAllHotels() {

		WebResource r = resource().path("hotel");
		
        String response = r.type(MediaType.APPLICATION_XML).get(String.class);
		System.out.print(response + "\n");
        Assert.assertTrue(response.contains("<hotels>"));
        Assert.assertTrue(response.contains("<hotel>"));
    }

    @Test
    public void testSearchHotels() {
        /*WebResource r = resource().path("hotel/search").queryParam("q", "Westin");
        String s = r.accept(MediaType.APPLICATION_XML).get(String.class);
        System.out.print(s + "\n");
        Assert.assertTrue(s.contains("Westin"));*/
    }

}

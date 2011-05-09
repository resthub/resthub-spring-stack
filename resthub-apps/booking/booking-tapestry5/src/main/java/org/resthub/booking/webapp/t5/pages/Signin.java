package org.resthub.booking.webapp.t5.pages;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.services.Request;

/**
 * Login page. TInspirated from tapestry-spring-security-sample
 * (http://www.localhost.nu/java/tapestry-spring-security/)
 * 
 * @author Robin Helgelin
 * @author bmeurant <Baptiste Meurant>
 */
public class Signin {

	private static final String FAILED = "failed";

	@Inject
	@Value("${spring-security.check.url}")
	private String checkUrl;

	@Inject
	private Request request;

	private boolean failed = false;

	public boolean isFailed() {
		return failed;
	}

	public String getLoginCheckUrl() {
		return request.getContextPath() + checkUrl;
	}

	public void onActivate(String extra) {
		if (extra.equals(FAILED)) {
			failed = true;
		}
	}
}
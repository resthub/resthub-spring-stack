package org.resthub.web.client;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.resthub.web.jackson.JacksonProvider;

import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;

public class ClientFactory {

    @SuppressWarnings("deprecation")
	public static ApacheHttpClient4 create() {
        DefaultApacheHttpClient4Config config = new DefaultApacheHttpClient4Config();
        config.getSingletons().add(new JacksonProvider());
        ApacheHttpClient4 jerseyClient = ApacheHttpClient4.create(config);
        HttpClient httpClient = jerseyClient.getClientHandler().getHttpClient();
        // Allow unsigned SSL certificates for testing purpose
        // TODO : control this thnaks to a properties value
        SSLSocketFactory sf = (SSLSocketFactory) httpClient.getConnectionManager().getSchemeRegistry().getScheme("https").getSchemeSocketFactory();
        sf.setHostnameVerifier(new AllowAllHostnameVerifier());
        return jerseyClient;
    }

}

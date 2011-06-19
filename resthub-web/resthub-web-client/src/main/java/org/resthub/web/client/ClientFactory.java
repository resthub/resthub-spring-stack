package org.resthub.web.client;

import org.resthub.web.jackson.JacksonProvider;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;

public class ClientFactory {

    public static Client create() {
        DefaultApacheHttpClient4Config config = new DefaultApacheHttpClient4Config();
        config.getSingletons().add(new JacksonProvider());
        return ApacheHttpClient4.create(config);
    }

}

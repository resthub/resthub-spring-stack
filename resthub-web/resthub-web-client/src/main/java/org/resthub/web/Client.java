package org.resthub.web;

import com.ning.http.client.*;
import com.ning.http.client.AsyncHttpClientConfig.Builder;
import com.ning.http.client.Realm.AuthScheme;
import com.ning.http.client.Realm.RealmBuilder;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Future;
import org.resthub.web.Response;
import org.resthub.web.oauth2.OAuth2Config;
import org.resthub.web.oauth2.OAuth2RequestFilter;
import org.resthub.web.support.*;

/**
 * RESThub AsyncHttpClient wrapper inspired from Play! Framework 2<br>
 *
 * Sample usage:
 * <pre> Client httpClient = new Client();
 * User user = httpClient.url("http://localhost:8080/api/user").jsonPost(user).get().resource(User.class);</pre>
 *
 */
public class Client implements Closeable {

    protected AsyncHttpClient client;
    protected Builder builder;
    protected OAuth2Config.Builder oAuth2ConfigBuilder;
    protected OAuth2Config oAuth2Config;
    protected List<BodyReader> bodyReaders = new ArrayList<>();
    protected List<BodyWriter> bodyWriters = new ArrayList<>();
    private String username = null;
    private String password = null;
    private AuthScheme scheme = null;

    /**
     * Create an HTTP client with default configuration
     */
    public Client() {
        this.builder = new Builder();
    }

    /**
     * Create an HTTP client with a pre-configured Builder
     *
     * @see Builder
     */
    public Client(Builder builder) {
        this.builder = builder;
    }

    /**
     * Set HTTP proxy configuration
     */
    public Client setProxy(String host, int port) {
        this.builder.setProxyServer(new ProxyServer(host, port));
        return this;
    }

    /**
     * Add a candidate BodyReader to read an HTTP response body to an object
     *
     * @param br a bodyreader
     * @see BodyReader
     */
    public Client addBodyReader(BodyReader br) {
        this.bodyReaders.add(br);
        return this;
    }

    /**
     * Add a candidate BodyWriter to serialize an object to an HTTP response
     * body
     *
     * @param br a bodyreader
     * @see BodyWriter
     */
    public Client addBodyWriter(BodyWriter bw) {
        this.bodyWriters.add(bw);
        return this;
    }

    /**
     * Sets the authentication header for the current client.
     *
     * @param username
     * @param password
     * @param scheme authentication scheme
     */
    public Client setAuth(String username, String password, AuthScheme scheme) {
        this.username = username;
        this.password = password;
        this.scheme = scheme;
        return this;
    }

    public Client setOAuth2Builder(OAuth2Config.Builder builder) {
        this.oAuth2ConfigBuilder = builder;

        return this;
    }

    /**
     * Sets the OAuth2 authentication header for the current client.
     *
     * @param username The user's login
     * @param password The user's password
     * @param accessTokenEndpoint URL of the OAuth2.0 access token endpoint
     * service
     * @param clientId id of the current application registered with the remote
     * OAuth2.0 provider
     * @param clientSecret secret of the current application registered with the
     * remote OAuth2.0 provider
     */
    public Client setOAuth2(String username, String password, String accessTokenEndpoint, String clientId, String clientSecret) {

        if (null == oAuth2ConfigBuilder) {
            oAuth2ConfigBuilder = new OAuth2Config.Builder();
        }
        oAuth2ConfigBuilder.setUsername(username).setPassword(password);
        oAuth2ConfigBuilder.setAccessTokenEndpoint(accessTokenEndpoint);
        oAuth2ConfigBuilder.setClientId(clientId).setClientSecret(clientSecret);
        return this;
    }

    /**
     * Prepare a new request. You can then construct it by chaining calls.
     *
     * @param url the URL to request
     */
    public RequestHolder url(String url) {
        if (this.client == null) {
            this.client = buildClient();
        }

        this.bodyReaders.add(new JsonBodyReader());
        this.bodyReaders.add(new XmlBodyReader());
        this.bodyWriters.add(new JsonBodyWriter());
        this.bodyWriters.add(new XmlBodyWriter());

        return new RequestHolder(url);
    }

    @Override
    public void close() {
        if (client != null) {
            client.close();
        }
    }

    private AsyncHttpClient buildClient() {

        if (oAuth2ConfigBuilder != null) {
            oAuth2Config = oAuth2ConfigBuilder.build();
            
            // TODO create request+response filters in OAuth2Config
            // -> handle new endpoints, refresh tokens...
            OAuth2RequestFilter oauth2Filter = new OAuth2RequestFilter(oAuth2Config.getAccessTokenEndpoint(), oAuth2Config.getClientId(), oAuth2Config.getClientSecret());
            oauth2Filter.setSchemeName(oAuth2Config.getOAuth2Scheme());
            builder.addRequestFilter(oauth2Filter);
        }

        return new AsyncHttpClient(builder.build());
    }

    /**
     * Provides the bridge between the wrapper and the underlying ning request
     */
    public class Request extends RequestBuilderBase<Request> {

        public Request(String method) {
            super(Request.class, method, false);
        }

        private Request auth(String username, String password, AuthScheme scheme) {
            this.setRealm((new RealmBuilder()).setScheme(scheme).setPrincipal(username).setPassword(password)
                    .setUsePreemptiveAuth(true).build());
            return this;
        }

        private Future<Response> execute() {
            Future<Response> future = null;
            AsyncEntityHandler handler = new AsyncEntityHandler();
            handler.setBodyReaders(bodyReaders);

            try {
                future = client.executeRequest(request, handler);
            } catch (IOException ex) {
                future.cancel(true);
            }
            return future;
        }
    }

    /**
     * provides the User facing API for building WS request.
     */
    public class RequestHolder {

        private final String url;
        private Map<String, Collection<String>> headers = new HashMap<>();
        private Map<String, Collection<String>> queryParameters = new HashMap<>();
        private List<Cookie> cookies = new ArrayList<>();
        private String body = null;

        public RequestHolder(String url) {
            this.url = url;
        }

        /**
         * Sets a header with the given name, this can be called repeatedly
         *
         * @param name
         * @param value
         */
        public RequestHolder setHeader(String name, String value) {
            if (headers.containsKey(name)) {
                Collection<String> values = headers.get(name);
                values.add(value);
            } else {
                List<String> values = new ArrayList<>();
                values.add(value);
                headers.put(name, values);
            }
            return this;
        }

        /**
         * Sets a query parameter with the given name,this can be called
         * repeatedly
         *
         * @param name
         * @param value
         */
        public RequestHolder setQueryParameter(String name, String value) {
            if (queryParameters.containsKey(name)) {
                Collection<String> values = headers.get(name);
                values.add(value);
            } else {
                List<String> values = new ArrayList<>();
                values.add(value);
                queryParameters.put(name, values);
            }
            return this;
        }

        /**
         * Adds a cookie
         * @param cookie
         * @return 
         */
        public RequestHolder addCookie(Cookie cookie) {
            cookies.add(cookie);
            return this;
        }

        /**
         * Perform a GET on the request asynchronously.
         */
        public Future<Response> get() {
            return execute("GET");
        }

        public Future<Response> getJson() {
            this.setHeader(Http.ACCEPT, Http.JSON);
            return execute("GET");
        }

        public Future<Response> getXml() {
            this.setHeader(Http.ACCEPT, Http.XML);
            return execute("GET");
        }

        /**
         * Perform a POST on the request asynchronously.
         *
         * @param body represented as String
         */
        public Future<Response> post(String body) {
            return executeString("POST", body);
        }

        public Future<Response> post() {
            return executeString("POST", this.body);
        }

        /**
         * Perform a PUT on the request asynchronously.
         *
         * @param body represented as String
         */
        public Future<Response> put(String body) {
            return executeString("PUT", body);
        }

        public Future<Response> jsonPut(Object o) {
            this.setHeader(Http.ACCEPT, Http.JSON);
            this.setHeader(Http.CONTENT_TYPE, Http.JSON);
            return executeString("PUT", serialize(Http.JSON, o));
        }

        public Future<Response> xmlPut(Object o) {
            this.setHeader(Http.ACCEPT, Http.XML);
            this.setHeader(Http.CONTENT_TYPE, Http.XML);
            return executeString("PUT", serialize(Http.XML, o));
        }

        /**
         * Perform a POST on the request asynchronously.
         *
         * @param body represented as an InputStream
         */
        public Future<Response> post(InputStream body) {
            return executeIS("POST", body);
        }

        public Future<Response> jsonPost(Object o) {
            this.setHeader(Http.ACCEPT, Http.JSON);
            this.setHeader(Http.CONTENT_TYPE, Http.JSON);
            return executeString("POST", serialize(Http.JSON, o));
        }

        public Future<Response> xmlPost(Object o) {
            this.setHeader(Http.ACCEPT, Http.XML);
            this.setHeader(Http.CONTENT_TYPE, Http.XML);
            return executeString("POST", serialize(Http.XML, o));
        }

        /**
         * Perform a PUT on the request asynchronously.
         *
         * @param body represented as an InputStream
         */
        public Future<Response> put(InputStream body) {
            return executeIS("PUT", body);
        }

        /**
         * Perform a POST on the request asynchronously.
         *
         * @param body represented as a File
         */
        public Future<Response> post(File body) {
            return executeFile("POST", body);
        }

        /**
         * Perform a PUT on the request asynchronously.
         *
         * @param body represented as a File
         */
        public Future<Response> put(File body) {
            return executeFile("PUT", body);
        }

        /**
         * Perform a DELETE on the request asynchronously.
         */
        public Future<Response> delete() {
            return execute("DELETE");
        }

        /**
         * Perform a HEAD on the request asynchronously.
         */
        public Future<Response> head() {
            return execute("HEAD");
        }

        /**
         * Perform a OPTION on the request asynchronously.
         */
        public Future<Response> option() {
            return execute("OPTION");
        }

        private Future<Response> execute(String method) {
            Request req = new Request(method).setUrl(url).setHeaders(headers)
                    .setQueryParameters(new FluentStringsMap(queryParameters));
            if (username != null && password != null && scheme != null) {
                req.auth(username, password, scheme);
            }
            if (oAuth2Config != null && oAuth2Config.getAccessTokenEndpoint() != null) {
                req.setRealm(new Realm.RealmBuilder().setPrincipal(oAuth2Config.getUsername()).setPassword(oAuth2Config.getPassword()).build());
            }
            addCookies(req);
            return req.execute();
        }

        private Future<Response> executeString(String method, String body) {
            Request req = new Request(method).setBody(body).setUrl(url).setHeaders(headers)
                    .setQueryParameters(new FluentStringsMap(queryParameters));
            if (username != null && password != null && scheme != null) {
                req.auth(username, password, scheme);
            }
            addCookies(req);
            return req.execute();
        }

        private Future<Response> executeIS(String method, InputStream body) {
            Request req = new Request(method).setBody(body).setUrl(url).setHeaders(headers)
                    .setQueryParameters(new FluentStringsMap(queryParameters));
            if (username != null && password != null && scheme != null) {
                req.auth(username, password, scheme);
            }
            addCookies(req);
            return req.execute();
        }

        private Future<Response> executeFile(String method, File body) {
            Request req = new Request(method).setBody(body).setUrl(url).setHeaders(headers)
                    .setQueryParameters(new FluentStringsMap(queryParameters));
            if (username != null && password != null && scheme != null) {
                req.auth(username, password, scheme);
            }
            addCookies(req);
            return req.execute();
        }

        private void addCookies(Request req) {
            for (Cookie cookie : cookies) {
                req.addCookie(cookie);
            }
        }

        private String serialize(String mediaType, Object o) {

            for (BodyWriter bw : bodyWriters) {
                if (bw.canWrite(mediaType)) {
                    return bw.writeEntity(mediaType, o);
                }
            }
            throw new RuntimeException("cannot serialize request body for mediaType " + mediaType);
        }
    }
}

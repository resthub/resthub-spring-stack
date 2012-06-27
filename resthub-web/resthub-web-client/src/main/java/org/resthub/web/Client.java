package org.resthub.web;

import com.ning.http.client.AsyncHttpClientConfig.Builder;
import com.ning.http.client.Realm.AuthScheme;
import com.ning.http.client.Realm.RealmBuilder;
import com.ning.http.client.*;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Future;
import org.resthub.web.oauth2.OAuth2RequestFilter;
import org.resthub.web.support.*;

/**
 * RESThub AsyncHttpClient wrapper inspired from Play Framework 2 one
 *
 * Sample usage : User user =
 * Client.url("http://localhost:8080/api/user".jsonPost(user).get().getEntity(User.class);
 *
 *
 */
public class Client implements Closeable {

    protected AsyncHttpClient client;
    protected Builder builder;
    protected List<BodyReader> bodyReaders = new ArrayList<>();
    protected List<BodyWriter> bodyWriters = new ArrayList<>();
    private String username = null;
    private String password = null;
    private String clientId = null;
    private String clientSecret = null;
    private String accessTokenEndpoint = null;
    private AuthScheme scheme = null;

    public Client() {
        this.builder = new Builder();
    }

    public Client(Builder builder) {
        this.builder = builder;
    }

    public Client setProxy(String host, int port) {
        this.builder.setProxyServer(new ProxyServer(host, port));
        return this;
    }

    public Client addBodyReader(BodyReader br) {
        this.bodyReaders.add(br);
        return this;
    }

    public Client addBodyWriter(BodyWriter bw) {
        this.bodyWriters.add(bw);
        return this;
    }

    /**
     * Sets the authentication header for the current request.
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

    /**
     * Sets the OAuth2 authentication header for the current request.
     *
     * @param username
     * @param password
     * @param accessTokenEndpoint
     * @param clientId
     * @param clientSecret
     */
    public Client setOAuth2(String username, String password, String accessTokenEndpoint, String clientId, String clientSecret) {
        this.username = username;
        this.password = password;
        this.accessTokenEndpoint = accessTokenEndpoint;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        builder.addRequestFilter(new OAuth2RequestFilter(accessTokenEndpoint, clientId, clientSecret));
        return this;
    }

    /**
     * Prepare a new request. You can then construct it by chaining calls.
     *
     * @param url the URL to request
     */
    public RequestHolder url(String url) {
        if (this.client == null) {
            this.client = new AsyncHttpClient(builder.build());
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

    /**
     * Provides the bridge between the wrapper and the underlying ning request
     */
    public class Request extends RequestBuilderBase<Request> {

        public Request(String method) {
            super(Request.class, method, false);
        }

        private Request auth(String username, String password, AuthScheme scheme) {
            this.setRealm((new RealmBuilder()).setScheme(scheme).setPrincipal(username).setPassword(password).setUsePreemptiveAuth(true).build());
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
            Request req = new Request(method).setUrl(url).setHeaders(headers).setQueryParameters(new FluentStringsMap(queryParameters));
            if (username != null && password != null && scheme != null) {
                req.auth(username, password, scheme);
            }
            if (accessTokenEndpoint != null && clientId != null && clientSecret != null && username != null && password != null) {
                req.setRealm(new Realm.RealmBuilder().setPrincipal(username).setPassword(password).build());
            }
            return req.execute();
        }

        private Future<Response> executeString(String method, String body) {
            Request req = new Request(method).setBody(body).setUrl(url).setHeaders(headers).setQueryParameters(new FluentStringsMap(queryParameters));
            if (username != null && password != null && scheme != null) {
                req.auth(username, password, scheme);
            }
            return req.execute();
        }

        private Future<Response> executeIS(String method, InputStream body) {
            Request req = new Request(method).setBody(body).setUrl(url).setHeaders(headers).setQueryParameters(new FluentStringsMap(queryParameters));
            if (username != null && password != null && scheme != null) {
                req.auth(username, password, scheme);
            }
            return req.execute();
        }

        private Future<Response> executeFile(String method, File body) {
            Request req = new Request(method).setBody(body).setUrl(url).setHeaders(headers).setQueryParameters(new FluentStringsMap(queryParameters));
            if (username != null && password != null && scheme != null) {
                req.auth(username, password, scheme);
            }
            return req.execute();
        }

        private String serialize(String mediaType, Object o) {

            for (BodyWriter bw : bodyWriters) {
                if(bw.canWrite(mediaType)) {
                    return bw.writeEntity(mediaType, o);
                }
            }
            throw new RuntimeException("cannot serialize request body for mediaType "+mediaType);
        }
    }
}

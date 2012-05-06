package org.resthub.web;

import com.ning.http.client.AsyncHttpClientConfig.Builder;
import com.ning.http.client.Realm.AuthScheme;
import com.ning.http.client.Realm.RealmBuilder;
import com.ning.http.client.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Future;
import org.resthub.web.Client.Request;
import org.resthub.web.oauth2.OAuth2RequestFilter;

/**
 * RESThub AsyncHttpClient wrapper inspired from Play Framework 2 one
 **/
public class Client {

    /**
     * Prepare a new request. You can then construct it by chaining calls.
     *
     * @param url the URL to request
     */
    public static RequestHolder url(String url) {
        return new RequestHolder(url);
    }

    /**
     * Provides the bridge between the wrapper and the underlying ning request
     */
    public static class Request extends RequestBuilderBase<Request> {

        private Builder builder = new Builder();
        
        public Request(String method) {
            super(Request.class, method, false);
        }

        private Request auth(String username, String password, AuthScheme scheme) {
            this.setRealm((new RealmBuilder())
                .setScheme(scheme)
                .setPrincipal(username)
                .setPassword(password)
                .setUsePreemptiveAuth(true)
                .build());
            return this;
        }
        
        public void setBuilder(Builder builder) {
            this.builder = builder;
        }
        
         public Builder getBuilder() {
            return this.builder;
        }

        private Future<Response> execute() {
            Future<Response> future = null;
            try {
                future = new AsyncHttpClient(builder.build()).executeRequest(request, new AsyncCompletionHandler<Response>() {
                @Override
                public Response onCompleted(com.ning.http.client.Response response) {
                    return new Response(response);
                }
            });
            } catch (IOException ex) {
                future.cancel(true);
            }
            return future;
        }
    }
    
    /**
     * provides the User facing API for building WS request.
     */
    public static class RequestHolder {

        private final String url;
        private Map<String, Collection<String>> headers = new HashMap<String, Collection<String>>();
        private Map<String, Collection<String>> queryParameters = new HashMap<String, Collection<String>>();

        private String body = null;
        private String username = null;
        private String password = null;
        private String clientId = null;
        private String clientSecret = null;
        private String accessTokenEndpoint = null;
        private AuthScheme scheme = null;
        private Builder builder = new Builder();

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
                List<String> values = new ArrayList<String>();
                values.add(value);
                headers.put(name, values);
            }
            return this;
        }

        /**
         * Sets a query parameter with the given name,this can be called repeatedly
         *
         * @param name
         * @param value
         */
        public RequestHolder setQueryParameter(String name, String value) {
            if (queryParameters.containsKey(name)) {
                Collection<String> values = headers.get(name);
                values.add(value);
            } else {
                List<String> values = new ArrayList<String>();
                values.add(value);
                queryParameters.put(name, values);
            }
            return this;
        }

        /**
         * Sets the authentication header for the current request.
         *
         * @param username
         * @param password
         * @param scheme authentication scheme
         */
        public RequestHolder setAuth(String username, String password, AuthScheme scheme) {
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
        public RequestHolder setOAuth2(String username, String password, String accessTokenEndpoint, String clientId, String clientSecret) {
            this.username = username;
            this.password = password;
            this.accessTokenEndpoint = accessTokenEndpoint;
            this.clientId = clientId;
            this.clientSecret = clientSecret;
            builder.addRequestFilter(new OAuth2RequestFilter(accessTokenEndpoint, clientId, clientSecret));
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
            return executeString("PUT", JsonHelper.serialize(o));
        }
        
        public Future<Response> xmlPut(Object o) {
            this.setHeader(Http.ACCEPT, Http.XML);
            this.setHeader(Http.CONTENT_TYPE, Http.XML);
            return executeString("PUT", XmlHelper.serialize(o));
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
            return executeString("POST", JsonHelper.serialize(o));
        }
        
        public Future<Response> xmlPost(Object o) {
            this.setHeader(Http.ACCEPT, Http.XML);
            this.setHeader(Http.CONTENT_TYPE, Http.XML);
            return executeString("POST", XmlHelper.serialize(o));
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
            if (this.username != null && this.password != null && this.scheme != null)
                req.auth(this.username, this.password, this.scheme);
            if(this.accessTokenEndpoint != null && this.clientId != null && this.clientSecret != null && this.username != null && this.password != null) {
                req.setBuilder(this.builder);
                req.setRealm(new Realm.RealmBuilder().setPrincipal(this.username).setPassword(this.password).build());
            }
            return req.execute();
        }

        private Future<Response> executeString(String method, String body) {
            Request req = new Request(method).setBody(body)
                                                 .setUrl(url)
                                                 .setHeaders(headers)
                                                 .setQueryParameters(new FluentStringsMap(queryParameters));
            if (this.username != null && this.password != null && this.scheme != null)
                req.auth(this.username, this.password, this.scheme);
            return req.execute();
        }

        private Future<Response> executeIS(String method, InputStream body) {
            Request req = new Request(method).setBody(body)
                                                 .setUrl(url)
                                                 .setHeaders(headers)
                                                 .setQueryParameters(new FluentStringsMap(queryParameters));
            if (this.username != null && this.password != null && this.scheme != null)
                req.auth(this.username, this.password, this.scheme);
            return req.execute();
        }

        private Future<Response> executeFile(String method, File body) {
            Request req = new Request(method).setBody(body)
                                                 .setUrl(url)
                                                 .setHeaders(headers)
                                                 .setQueryParameters(new FluentStringsMap(queryParameters));
            if (this.username != null && this.password != null && this.scheme != null)
                req.auth(this.username, this.password, this.scheme);
            return req.execute();
        }


    }
    
    /**
     * A WS response.
     */
    public static class Response {

        private com.ning.http.client.Response ahcResponse;

        public Response(com.ning.http.client.Response ahcResponse) {
            this.ahcResponse = ahcResponse;
        }

        /**
         * Get the HTTP status code of the response
         */
        public int getStatus() {
            return ahcResponse.getStatusCode();
        }

        /**
         * Get the given HTTP header of the response
         */
        public String getHeader(String key) {
            return ahcResponse.getHeader(key);
        }

        /**
         * Get the response body as a string
         */
        public String getBody() {
            try {
                return ahcResponse.getResponseBody();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Deserialize a JSON response
         */
        public <T> T jsonDeserialize(Class<T> type) {
            try {
                return JsonHelper.deserialize(ahcResponse.getResponseBody(), type);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Deserialize a XML response
         */
        public <T> T xmlDeserialize(Class<T> type) {
            try {
                return XmlHelper.deserialize(ahcResponse.getResponseBody(), type);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }       

    }

}

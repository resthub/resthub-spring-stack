package org.resthub.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ning.http.client.cookie.Cookie;
import org.resthub.web.support.BodyReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Response {

    private com.ning.http.client.Response ahcResponse;
    private List<BodyReader> bodyReaders = new ArrayList<BodyReader>();

    public Response(com.ning.http.client.Response ahcResponse) {
        this.ahcResponse = ahcResponse;
    }

    public void addBodyReader(BodyReader br) {
        this.bodyReaders.add(br);
    }

    public void addBodyReaders(List<BodyReader> bodyReaders) {
        this.bodyReaders.addAll(bodyReaders);
    }

    public <T> T resource(Class<T> type) {
        try {
            for (BodyReader br : this.bodyReaders) {
                if (br.canRead(ahcResponse)) {
                    return br.readEntity(ahcResponse, type);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("unsupported media type " + ahcResponse.getContentType());
    }

    public <T> T resource(Class<T> type, String charset) {
        try {
            for (BodyReader br : this.bodyReaders) {
                if (br.canRead(ahcResponse)) {
                    return br.readEntity(ahcResponse, type, charset);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("unsupported media type " + ahcResponse.getContentType());
    }

    public <T> T resource(TypeReference valueTypeRef) {
        try {
            for (BodyReader br : this.bodyReaders) {
                if (br.canRead(ahcResponse)) {
                    return br.readEntity(ahcResponse, valueTypeRef);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("unsupported media type " + ahcResponse.getContentType());
    }

    public <T> T resource(TypeReference valueTypeRef, String charset) {
        try {
            for (BodyReader br : this.bodyReaders) {
                if (br.canRead(ahcResponse)) {
                    return br.readEntity(ahcResponse, valueTypeRef, charset);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("unsupported media type " + ahcResponse.getContentType());
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
     * Get the response body as a string with charset as parameter
     */
    public String getBody(String charset) {
        try {
            return ahcResponse.getResponseBody(charset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the cookies sent along the response
     */
    public List<Cookie> getCookies() {
        return ahcResponse.getCookies();
    }
}

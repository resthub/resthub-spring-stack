package org.resthub.web.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ning.http.client.Response;
import org.resthub.web.Http;
import org.resthub.web.XmlHelper;

import java.io.IOException;

public class XmlBodyReader implements BodyReader {

    @Override
    public boolean canRead(Response response) {
        return (response.getContentType() != null && (response.getContentType().startsWith(Http.XML) || response
                .getContentType().endsWith("+xml")));
    }

    @Override
    public <T> T readEntity(Response resp, Class<T> entityClass) throws IOException {
        return XmlHelper.deserialize(resp.getResponseBody(), entityClass);
    }

    @Override
    public <T> T readEntity(Response resp, Class<T> entityClass, String charset) throws IOException {
        return XmlHelper.deserialize(resp.getResponseBody(charset), entityClass);
    }

    @Override
    public <T> T readEntity(Response resp, TypeReference valueTypeRef) throws IOException {
        return XmlHelper.deserialize(resp.getResponseBody(), valueTypeRef);
    }

    @Override
    public <T> T readEntity(Response resp, TypeReference valueTypeRef, String charset) throws IOException {
        return XmlHelper.deserialize(resp.getResponseBody(charset), valueTypeRef);
    }
}

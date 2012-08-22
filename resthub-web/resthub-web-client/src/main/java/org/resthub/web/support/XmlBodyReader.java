package org.resthub.web.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ning.http.client.Response;
import java.io.IOException;
import org.resthub.web.Http;
import org.resthub.web.XmlHelper;

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
    public <T> T readEntity(Response resp, TypeReference valueTypeRef) throws IOException {
        return XmlHelper.deserialize(resp.getResponseBody(), valueTypeRef);
    }
}

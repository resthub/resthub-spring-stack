package org.resthub.web.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ning.http.client.Response;
import java.io.IOException;

public interface BodyReader {

    public boolean canRead(Response response);

    public <T> T readEntity(Response resp, Class<T> entityClass) throws IOException;

    public <T> T readEntity(Response resp, TypeReference valueTypeRef) throws IOException;
}

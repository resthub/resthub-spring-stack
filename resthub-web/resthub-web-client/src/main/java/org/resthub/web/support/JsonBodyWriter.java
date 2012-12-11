package org.resthub.web.support;

import org.resthub.web.Http;
import org.resthub.web.JsonHelper;

public class JsonBodyWriter implements BodyWriter {

    @Override
    public boolean canWrite(String mediaType) {
        return (mediaType != null && (mediaType.startsWith(Http.JSON) || mediaType.endsWith("+json")));
    }

    @Override
    public String writeEntity(String mediaType, Object object) {
        return JsonHelper.serialize(object);
    }

}

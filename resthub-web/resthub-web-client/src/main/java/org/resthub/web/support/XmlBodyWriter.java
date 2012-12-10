package org.resthub.web.support;

import org.resthub.web.Http;
import org.resthub.web.XmlHelper;

public class XmlBodyWriter implements BodyWriter {

    @Override
    public boolean canWrite(String mediaType) {
        return (mediaType != null && (mediaType.startsWith(Http.XML) || mediaType.endsWith("+xml")));
    }

    @Override
    public String writeEntity(String mediaType, Object object) {
        return XmlHelper.serialize(object);
    }
}

package org.resthub.web.support;


public interface BodyWriter {

    public boolean canWrite(String mediaType);

    public String writeEntity(String mediaType, Object object);

}

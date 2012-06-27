package org.resthub.web.support;

import org.resthub.web.Client;

public interface BodyWriter {
  
    public boolean canWrite(String mediaType);

    public String writeEntity(String mediaType, Object object);

}

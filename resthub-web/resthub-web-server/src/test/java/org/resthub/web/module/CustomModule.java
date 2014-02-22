package org.resthub.web.module;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.resthub.web.model.Book;

/**
 * User: jripault
 * Date: 22/02/14
 */
public class CustomModule extends SimpleModule {
    public CustomModule() {
        addSerializer(Book.class, new BookSerializer());
    }
}

package org.resthub.web.module;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.resthub.web.model.Book;

import java.io.IOException;

public final class BookSerializer extends StdScalarSerializer<Book> {

    public BookSerializer() {
        super(Book.class);
    }

    @Override
    public void serialize(Book value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeString(value.getTitle() + " written by " + value.getAuthor());
    }
}

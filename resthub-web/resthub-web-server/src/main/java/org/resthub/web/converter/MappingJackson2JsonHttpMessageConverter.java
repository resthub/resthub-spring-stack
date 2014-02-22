package org.resthub.web.converter;

/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.resthub.common.view.DataView;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;

/**
 * Implementation of {@link org.springframework.http.converter.HttpMessageConverter HttpMessageConverter} that can read
 * and write JSON using <a href="http://jackson.codehaus.org/">Jackson 2's</a> {@link com.fasterxml.jackson.databind.ObjectMapper}.
 *
 * <p>
 * This converter can be used to bind to typed beans, or untyped {@link java.util.HashMap HashMap} instances.
 *
 * <p>
 * By default, this converter supports {@code application/json}. This can be overridden by setting the
 * {@link #setSupportedMediaTypes(java.util.List) supportedMediaTypes} property.
 *
 * <p>
 * This converter also support custom JSON views, see RESThub documentation for more details
 *
 */
public class MappingJackson2JsonHttpMessageConverter extends MappingJackson2HttpMessageConverter {
    private String innerJsonPrefix;

    @Override
    public void setJsonPrefix(String jsonPrefix) {
        super.setJsonPrefix(jsonPrefix);
        innerJsonPrefix = jsonPrefix;
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {

        JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
        JsonGenerator jsonGenerator = this.getObjectMapper().getFactory().createGenerator(outputMessage.getBody(),
            encoding);

        try {
            if (this.innerJsonPrefix != null) {
                jsonGenerator.writeRaw(this.innerJsonPrefix);
            }
            // support for JsonView
            if (object instanceof DataView && ((DataView) object).hasView())
            {
                ObjectWriter writer = this.getObjectMapper().writerWithView(((DataView) object).getView());
                writer.writeValue(jsonGenerator, ((DataView) object).getData());
            } else {
                this.getObjectMapper().writeValue(jsonGenerator, object);
            }
        } catch (JsonProcessingException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }


}

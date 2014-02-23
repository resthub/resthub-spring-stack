package org.resthub.web.test;

import com.fasterxml.jackson.core.type.TypeReference;
import org.fest.assertions.api.Assertions;
import org.resthub.test.AbstractWebTest;
import org.testng.annotations.Test;

import java.util.List;

public class JacksonCustomModuleTest extends AbstractWebTest {

    public JacksonCustomModuleTest() {
        // force starting a new server on a different port because we need to reload the all context
        super("resthub-web-server,dots", 9798);
        this.startServerOnce = false;
        this.contextLocations = "classpath*:resthubContext.xml classpath*:applicationContext.xml classpath*:resthubContextCustomModule.xml";
    }

    @Test
    public void testWithCustomModuleJson() {
        List<String> books = this.request("book").jsonGet().resource(new TypeReference<List<String>>() {});
        Assertions.assertThat(books).isNotNull();
        Assertions.assertThat(books.size()).isEqualTo(2);
        Assertions.assertThat(books).contains("Effective Java written by Joshua Bloch");
        Assertions.assertThat(books).contains("Breaking Dawn written by Stephanie Myers");
    }
}

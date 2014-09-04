package org.resthub.web.test;

import com.fasterxml.jackson.core.type.TypeReference;
import org.fest.assertions.api.Assertions;
import org.resthub.test.AbstractWebTest;
import org.testng.annotations.Test;

public class NoSuffixDotsInUrlTest extends AbstractWebTest {

    public NoSuffixDotsInUrlTest() {
        // force starting a new server on a different port because we need to reload the all context
        super("resthub-web-server,dots", 9798);
        this.startServerOnce = false;
        this.contextLocations = "classpath*:resthubContext.xml classpath*:applicationContext.xml classpath*:resthubContextNoSuffixDots.xml";
    }

    @Test
    public void testParamsWithoutDot() {
        String param = this.request("dot/noDot").jsonGet().resource(String.class);
        Assertions.assertThat(param).isNotNull();
        Assertions.assertThat(param).isEqualTo("noDot");

        param = this.request("dot/noDot.json").jsonGet().resource(String.class);
        Assertions.assertThat(param).isNotNull();
        Assertions.assertThat(param).isEqualTo("noDot.json");

        param = this.request("dot/noDot.xml").xmlGet().getBody();
        Assertions.assertThat(param).isNotNull();
        Assertions.assertThat(param).contains("<String>noDot.xml</String>");

        param = this.request("dot/noDot.any").jsonGet().resource(String.class);
        Assertions.assertThat(param).isNotNull();
        Assertions.assertThat(param).isEqualTo("noDot.any");
    }

    @Test
    public void testParamsWithDot() {
        String param = this.request("dot/with.dot").jsonGet().resource(new TypeReference<String>() {});
        Assertions.assertThat(param).isNotNull();
        Assertions.assertThat(param).isEqualTo("with.dot");

        param = this.request("dot/with.dot.json").jsonGet().resource(String.class);
        Assertions.assertThat(param).isNotNull();
        Assertions.assertThat(param).isEqualTo("with.dot.json");

        param = this.request("dot/with.dot.xml").xmlGet().getBody();
        Assertions.assertThat(param).isNotNull();
        Assertions.assertThat(param).contains("<String>with.dot.xml</String>");
    }

    @Test
    public void testMappingWithDot() {
        String param = this.request("dot/dot.included").jsonGet().resource(new TypeReference<String>() {});
        Assertions.assertThat(param).isNotNull();
        Assertions.assertThat(param).isEqualTo("included");

        param = this.request("dot/dot.included.json").jsonGet().resource(String.class);
        Assertions.assertThat(param).isNotNull();
        Assertions.assertThat(param).isEqualTo("included.json");

        param = this.request("dot/dot.included.xml").xmlGet().getBody();
        Assertions.assertThat(param).isNotNull();
        Assertions.assertThat(param).contains("<String>included.xml</String>");
    }

}

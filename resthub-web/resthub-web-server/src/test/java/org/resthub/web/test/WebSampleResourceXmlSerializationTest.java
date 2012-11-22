package org.resthub.web.test;

import org.fest.assertions.api.Assertions;
import org.resthub.web.XmlHelper;
import org.resthub.web.model.Sample;
import org.testng.annotations.Test;

public class WebSampleResourceXmlSerializationTest {

    @Test
    public void testWebSampleResourceXmlSerialization() {
        Sample resource = new Sample("testResource");
        String output = XmlHelper.serialize(resource);
        Assertions.assertThat(output).contains("testResource");
    }

}
package org.resthub.common.util;

import org.fest.assertions.api.Assertions;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.inject.Inject;

@ActiveProfiles("resthub-common")
@ContextConfiguration(locations = { "classpath:resthubContext.xml" })
public class PostInitializeTest extends AbstractTestNGSpringContextTests {

    @Inject
    private Initializer initializer;

    @Test
    public void testPostInitialize() {
        Assertions.assertThat(initializer.getValue()).isTrue();
    }
}
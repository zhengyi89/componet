package com.zbjdl.common.demo;

import org.junit.BeforeClass;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * title: <br/>
 * description: 描述<br/>
 *
 */
@ActiveProfiles("test")
@DirtiesContext
public class SpringTransactionalTests extends AbstractJUnit4SpringContextTests {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

}

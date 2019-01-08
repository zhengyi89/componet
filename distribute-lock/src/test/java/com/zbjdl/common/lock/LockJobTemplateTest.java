package com.zbjdl.common.lock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zbjdl.common.lock.LockJobTemplate;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LockApplication.class)

public class LockJobTemplateTest {
	@Autowired
	LockJobTemplate lockJobTemplate;
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		SomeJob job = new SomeJob();
		String name = "Hero";
		lockJobTemplate.doJob("lockjob1", job, name);
	}

}

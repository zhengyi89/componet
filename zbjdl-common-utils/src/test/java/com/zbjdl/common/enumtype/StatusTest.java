package com.zbjdl.common.enumtype;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StatusTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Status status = Status.NORMAL;
		System.out.println(status);
		System.out.println(status.name());
		System.out.println(status.getDescription());

		assertTrue(status.equals(Status.NORMAL));

		for (Status type : Status.values()) {
			System.out.println(type);
		}

		Status aStatus = Status.valueOf("NORMAL");

		assertTrue(status.equals(aStatus));

		status = Status.NORMAL;
		switch (status) {
		case NORMAL:
			System.out.println("Status.NORMAL");
			break;
		case WARNING:
			System.out.println("Status.WARNING");
			break;
		case ERROR:
			System.out.println("Status.ERROR");
			break;
		default:
			System.out.println("unknown");
			break;
		}

	}

}

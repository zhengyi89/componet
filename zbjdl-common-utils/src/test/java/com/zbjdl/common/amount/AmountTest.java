package com.zbjdl.common.amount;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.zbjdl.common.amount.Amount;

public class AmountTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testHashCode() {
		fail("Not yet implemented");
	}

	@Test
	public void testAmount() {
		Amount income = new Amount();
		
		System.out.println(income.toString());
		assertEquals("0.00", income.toString());
		
	
	}

	@Test
	public void testAmountBigDecimal() {
		double value = 12000.2532;
		Amount income = new Amount(value,4);
		assertEquals("12000.2532", income.toString());
		BigDecimal bigIncome = new BigDecimal(value);
		System.out.println(bigIncome);
		System.out.println(income.getValue());
		assertNotEquals(income.getValue(), bigIncome);
		
		value=12000.22;
		income = new Amount(value,2);
		assertEquals("12000.22", income.toString());
		bigIncome = new BigDecimal(value);
		System.out.println(bigIncome);
		System.out.println(income.getValue());
		assertNotEquals(income.getValue(), bigIncome);
		
		
		
	}

	@Test
	public void testAmountLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testAmountLongInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testAmountDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testAmountDoubleInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testAmountInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testAmountIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testAmountString() {
		fail("Not yet implemented");
	}

	@Test
	public void testAmountStringInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testAmountBigDecimalInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsGreaterThan() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualTo() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsLesserThan() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsPositive() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsZero() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsNegative() {
		fail("Not yet implemented");
	}

	@Test
	public void testAdd() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubtract() {
		fail("Not yet implemented");
	}

	@Test
	public void testMultiplyPercent() {
		fail("Not yet implemented");
	}

	@Test
	public void testMultiplyLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testMultiplyInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testMultiplyDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetValue() {
		fail("Not yet implemented");
	}

	@Test
	public void testEqualsObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

}

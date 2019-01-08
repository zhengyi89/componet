package com.zbjdl.common.enumtype;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FeeTypeTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		FeeType feeType = FeeType.LOAN_FEE;
		System.out.println(feeType);
		System.out.println(feeType.name());
		System.out.println(feeType.getDescription());
		System.out.println(feeType.getIndex());
		
		
		assertTrue(feeType.equals(FeeType.LOAN_FEE));
		
        for(FeeType type : FeeType.values()){
            System.out.println(type);
        }
        
        System.out.println(FeeType.getName(3));
        FeeType aFeeType = FeeType.valueOf("LOAN_FEE");
        
		assertTrue(feeType.equals(aFeeType));

	}

}

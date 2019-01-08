package com.zbjdl.common.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zbjdl.common.utils.UIDGenerator;

public class UIDGeneratorTest {

	@Test
	public void testNextLong() {
		List<String> list = new ArrayList<String>();
		for(int i=0;i<=99999999;i++){
			String sequence = UIDGenerator.next(i);
			System.out.println(sequence);
			if(!list.contains(sequence)){
				list.add(sequence);
				
			}else{
				fail();
			}
			
		}
	}

}

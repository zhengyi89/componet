package com.zbjdl.common.demo.service.impl;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.zbjdl.common.amount.Amount;
import com.zbjdl.common.demo.SpringTransactionalTests;
import com.zbjdl.common.demo.model.Order;
import com.zbjdl.common.demo.service.OrderService;
import com.zbjdl.common.respository.OptimisticLockingException;
@ContextConfiguration(locations = {"/order-spring/order-application.xml"})
public class OrderServiceImplTest extends SpringTransactionalTests{

	@Autowired
	private OrderService orderService;
	@Before
	public void setUp() throws Exception {
	}


	@Test
	public void testSave() {
		
		List<String> itemNames = new ArrayList<String>(){{
	        add("item1");
	        add("item2");
	        add("stringN");
	    }};
		Order order = new Order();
		order.setCreatedDate(new Date());
		order.setLastModifiedDate(new Date());
		order.setAmount(new Amount(2000.22));
		System.out.println(order.getAmount());
		order.setProductName("Apple");
		order.setOrderNumber("NO000000001");
		order.setStatus("1");
		order.setItemNames(itemNames);
		order.setCreatedDate(new Date());
		orderService.create(order);
		System.out.println("id:"+order.getId());
		
		
	}
	
	@Test
	public void testselectById() {
		Order order = orderService.selectById(1L);
		assertNotNull(order);
		assertFalse(order.getItemNames().isEmpty());
		
	}

	@Test
	public void testUpdate() {
        Order order = orderService.selectById(1L);
        order.setProductName("iphone6");

        order.setAmount(new Amount(5888.00));;
        orderService.update(order);

	}

    @Test
    public void testOptimisticLocking() throws Exception {
        Order order = orderService.selectById(1L);
        order.setProductName("testProduct");

        orderService.update(order);
        try {
        	order.setVersion(2L);
        	orderService.update(order);
            fail("乐观锁异常没有抛出");
        } catch (OptimisticLockingException e) {
            fail("what happened?");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }	

}

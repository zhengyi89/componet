package com.zbjdl.common.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.zbjdl.common.demo.model.Order;
import com.zbjdl.common.demo.repository.OrderRepository;
import com.zbjdl.common.demo.service.OrderService;
@Component("orderService")
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderRepository orderRepository;
	

	@Override
	public Order selectById(Long id) {
		return orderRepository.selectById(id);
	}

	@Override
	public void update(Order order) {
		orderRepository.update(order);
	}

	@Override
	public void create(Order order) {
		orderRepository.save(order);
	}

}

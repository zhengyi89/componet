package com.zbjdl.common.demo.service;

import com.zbjdl.common.demo.model.Order;

public interface OrderService {

    Order selectById(Long id);

    void update(Order order);
    
    void create(Order order);

}

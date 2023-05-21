package com.qjc.midjourney.dao;

import com.qjc.midjourney.dto.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDao {

    int insert(Order order);

    int update(Order order);

    Order findById(String id);

    List<Order> findByPrompt(String prompt);
}

package com.example.demo.Services;

import java.util.List;

import com.example.demo.Models.Order;

public interface OrderService {
    Order createOrder(Order order);

    Order updateOrderStatus(Long orderId, String status);

    Order getOrderById(Long orderId);

    List<Order> getAllOrders();
}

package com.example.demo.Services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.demo.Models.Order;

@Service
public class OrderServiceImpl implements OrderService {

    // Thread-safe list to store orders
    private final List<Order> orders = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong orderIdGenerator = new AtomicLong();

    @Override
    public Order createOrder(Order order) {
        long orderId = orderIdGenerator.incrementAndGet();
        order.setId(orderId);
        orders.add(order);
        return order;
    }

    @Override
    public Order updateOrderStatus(Long orderId, String status) {
        synchronized (orders) {
            for (Order order : orders) {
                if (order.getId().equals(orderId)) {
                    order.setStatus(status);
                    return order;
                }
            }
        }
        return null; // Or throw an exception if the order is not found
    }

    @Override
    public Order getOrderById(Long orderId) {
        synchronized (orders) {
            for (Order order : orders) {
                if (order.getId().equals(orderId)) {
                    return order;
                }
            }
        }
        return null; // Or throw an exception if the order is not found
    }

    @Override
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders); // Return a copy to avoid modification outside the service
    }
}

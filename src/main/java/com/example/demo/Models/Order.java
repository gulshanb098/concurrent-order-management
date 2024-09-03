package com.example.demo.Models;
import java.util.List;

import lombok.Data;

@Data
public class Order {
    private Long id;
    private String status; 
    private List<OrderItem> items;
}


package com.example.demo.Models;

import lombok.Data;

@Data
public class OrderItem {
    private Long id;
    private String productName;
    private int quantity;
}
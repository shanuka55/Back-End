package com.shopProject.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {
    private long orderId;
    private long customerId;
    private long productId;
    private int quantity;
    private LocalDate orderDate;
}

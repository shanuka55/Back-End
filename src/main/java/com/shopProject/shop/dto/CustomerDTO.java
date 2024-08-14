package com.shopProject.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO {
    private long customerId;
    private String name;
    private String email;
    private String phoneNumber;
}

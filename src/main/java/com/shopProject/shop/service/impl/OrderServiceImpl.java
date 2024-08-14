package com.shopProject.shop.service.impl;

import com.shopProject.shop.dto.OrderDTO;
import com.shopProject.shop.dto.ProductDTO;
import com.shopProject.shop.entity.Customer;
import com.shopProject.shop.entity.Order;
import com.shopProject.shop.entity.Product;
import com.shopProject.shop.repository.CustomerRepository;
import com.shopProject.shop.repository.OrderRepository;
import com.shopProject.shop.repository.ProductRepository;
import com.shopProject.shop.service.OrderService;
import com.shopProject.shop.util.VarListUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<OrderDTO> getAllOrders() {
        List<Order> users=orderRepository.findAll();
        return modelMapper.map(users, new TypeToken<ArrayList<OrderDTO>>() {
        }.getType());
    }

    public OrderDTO getUniqueOrder(long id) {
        Order order= orderRepository.findById(id).orElse(null);
        return modelMapper.map(order,OrderDTO.class);
    }

    public String saveOrder(OrderDTO orderDTO) {
        if (orderRepository.existsById(orderDTO.getOrderId())) {
            return VarListUtil.RSP_NO_DATA_FOUND;
        } else {
            Order order = new Order();
            order.setOrderId(orderDTO.getOrderId());
            order.setQuantity(orderDTO.getQuantity());
            order.setOrderDate(orderDTO.getOrderDate());

            // Fetch the customer and product entities using their respective IDs
            Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            Product product = productRepository.findById(orderDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Set the customer and product for the order
            order.setCustomer(customer);
            order.setProduct(product);

            // Save the order record
            orderRepository.save(order);
            return VarListUtil.RSP_SUCCESS;
        }
    }

    public String updateOrder(OrderDTO orderDTO){
        if (orderRepository.existsById(orderDTO.getOrderId())){
            orderRepository.save(modelMapper.map(orderDTO,Order.class));
            return VarListUtil.RSP_SUCCESS;
        }else {
            return VarListUtil.RSP_NO_DATA_FOUND;
        }

    }

    public String deleteOrder(long id) {
        if (orderRepository.existsByOrderId(id)) {
            orderRepository.deleteByOrderId(id);
            return VarListUtil.RSP_SUCCESS;
        } else {
            return VarListUtil.RSP_NO_DATA_FOUND;
        }
    }

}

package com.shopProject.shop.service.impl;

import com.shopProject.shop.dto.ProductDTO;
import com.shopProject.shop.entity.Product;
import com.shopProject.shop.repository.ProductRepository;
import com.shopProject.shop.service.ProductService;
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
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ProductDTO> getAllProducts() {
        List<Product> users=productRepository.findAll();
        return modelMapper.map(users, new TypeToken<ArrayList<ProductDTO>>() {
        }.getType());
    }

    public ProductDTO getUniqueProduct(long id) {
        Product product= productRepository.findById(id).orElse(null);
        return modelMapper.map(product,ProductDTO.class);
    }

    public String saveProduct(ProductDTO productDTO){
        if (productRepository.existsById(productDTO.getProductId())) {
            return VarListUtil.RSP_NO_DATA_FOUND;
        } else {
            productRepository.addProductRecord((int) productDTO.getProductId(),productDTO.getName()
                    ,productDTO.getPrice(),productDTO.getStock());
            return VarListUtil.RSP_SUCCESS;
        }

    }

    public String updateProduct(ProductDTO productDTO){
        if (productRepository.existsById(productDTO.getProductId())){
            productRepository.save(modelMapper.map(productDTO,Product.class));
            return VarListUtil.RSP_SUCCESS;
        }else {
            return VarListUtil.RSP_NO_DATA_FOUND;
        }

    }

    public String deleteProduct(long id) {
        if (productRepository.existsByProductId(id)) {
            productRepository.deleteByProductId(id);
            return VarListUtil.RSP_SUCCESS;
        } else {
            return VarListUtil.RSP_NO_DATA_FOUND;
        }
    }
}

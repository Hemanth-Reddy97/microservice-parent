package com.example.productmicroservice.service;

import com.example.productmicroservice.model.CreateProductRestModel;

public interface ProductService {
    String createProduct(CreateProductRestModel createProduct) throws  Exception;
}

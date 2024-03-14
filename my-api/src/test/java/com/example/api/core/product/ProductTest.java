package com.example.api.core.product;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class ProductTest {
    @Test
    void testCreateProduct(){
        var product = new Product(1, "Macbook", 10, "GZ");
        assertEquals("Macbook", product.getName());
    }
}

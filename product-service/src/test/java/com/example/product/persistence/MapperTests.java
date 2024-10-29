package com.example.product.persistence;


import com.example.api.core.product.Product;
import com.example.product.services.ProductMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.Assert.*;

public class MapperTests {

    private ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @Test
    public void mapperTests() {

        assertNotNull(mapper);

        Product api = new Product(1, "n", 1, "sa");

        ProductEntity entity = mapper.apiToEntity(api);

        Assertions.assertEquals(api.productId(), entity.productId());
        Assertions.assertEquals(api.productId(), entity.productId());
        Assertions.assertEquals(api.name(), entity.name());
        Assertions.assertEquals(api.weight(), entity.weight());

        Product api2 = mapper.entityToApi(entity);

        Assertions.assertEquals(api.productId(), api2.productId());
        Assertions.assertEquals(api.productId(), api2.productId());
        Assertions.assertEquals(api.name(), api2.name());
        Assertions.assertEquals(api.weight(), api2.weight());
        Assertions.assertNull(api2.serviceAddress());
    }
}
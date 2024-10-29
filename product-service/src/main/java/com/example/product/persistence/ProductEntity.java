package com.example.product.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="products")
public record ProductEntity (

    @Id
     String id,

    @Version
     Integer version,

    @Indexed(unique = true)
     int productId,

     String name,
     int weight){



}
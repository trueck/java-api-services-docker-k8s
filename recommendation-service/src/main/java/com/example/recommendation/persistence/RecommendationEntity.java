package com.example.recommendation.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "recommendations")
@CompoundIndex(name = "prod-rec-id", unique = true, def = "{'productId': 1, 'recommendationId' : 1}")
public record RecommendationEntity(

        @Id
        String id,

    @Version
                Integer version,

        int productId,
        int recommendationId,
        String author,
        int rating,
        String content
){

        }
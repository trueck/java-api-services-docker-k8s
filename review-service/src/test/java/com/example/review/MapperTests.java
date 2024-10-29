package com.example.review;

import com.example.api.core.review.Review;
import com.example.review.persistence.ReviewEntity;
import com.example.review.services.ReviewMapper;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class MapperTests {

    private ReviewMapper mapper = Mappers.getMapper(ReviewMapper.class);


    @Test
    public void mapperTests() {

        assertNotNull(mapper);

        Review api = new Review(1, 2, "a", "s", "C", "adr");

        ReviewEntity entity = mapper.apiToEntity(api);

        assertEquals(api.productId(), entity.getProductId());
        assertEquals(api.reviewId(), entity.getReviewId());
        assertEquals(api.author(), entity.getAuthor());
        assertEquals(api.subject(), entity.getSubject());
        assertEquals(api.content(), entity.getContent());

        Review api2 = mapper.entityToApi(entity);

        assertEquals(api.productId(), api2.productId());
        assertEquals(api.reviewId(), api2.reviewId());
        assertEquals(api.author(), api2.author());
        assertEquals(api.subject(), api2.subject());
        assertEquals(api.content(), api2.content());
        assertNull(api2.serviceAddress());
    }

    @Test
    public void mapperListTests() {

        assertNotNull(mapper);

        Review api = new Review(1, 2, "a", "s", "C", "adr");
        List<Review> apiList = Collections.singletonList(api);

        List<ReviewEntity> entityList = mapper.apiListToEntityList(apiList);
        assertEquals(apiList.size(), entityList.size());

        ReviewEntity entity = entityList.get(0);

        assertEquals(api.productId(), entity.getProductId());
        assertEquals(api.reviewId(), entity.getReviewId());
        assertEquals(api.author(), entity.getAuthor());
        assertEquals(api.subject(), entity.getSubject());
        assertEquals(api.content(), entity.getContent());

        List<Review> api2List = mapper.entityListToApiList(entityList);
        assertEquals(apiList.size(), api2List.size());

        Review api2 = api2List.get(0);

        assertEquals(api.productId(), api2.productId());
        assertEquals(api.reviewId(), api2.reviewId());
        assertEquals(api.author(), api2.author());
        assertEquals(api.subject(), api2.subject());
        assertEquals(api.content(), api2.content());
        assertNull(api2.serviceAddress());
    }
}
package com.example.recommendation;

import com.example.api.core.recommendation.Recommendation;
import com.example.recommendation.persistence.RecommendationEntity;
import com.example.recommendation.services.RecommendationMapper;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class MapperTests {

    private RecommendationMapper mapper = Mappers.getMapper(RecommendationMapper.class);

    @Test
    public void mapperTests() {

        assertNotNull(mapper);

        Recommendation api = new Recommendation(1, 2, "a", 4, "C", "adr");

        RecommendationEntity entity = mapper.apiToEntity(api);

        assertEquals(api.productId(), entity.productId());
        assertEquals(api.recommendationId(), entity.recommendationId());
        assertEquals(api.author(), entity.author());
        assertEquals(api.rate(), entity.rating());
        assertEquals(api.content(), entity.content());

        Recommendation api2 = mapper.entityToApi(entity);

        assertEquals(api.productId(), api2.productId());
        assertEquals(api.recommendationId(), api2.recommendationId());
        assertEquals(api.author(), api2.author());
        assertEquals(api.rate(), api2.rate());
        assertEquals(api.content(), api2.content());
        assertNull(api2.serviceAddress());
    }

    @Test
    public void mapperListTests() {

        assertNotNull(mapper);

        Recommendation api = new Recommendation(1, 2, "a", 4, "C", "adr");
        List<Recommendation> apiList = Collections.singletonList(api);

        List<RecommendationEntity> entityList = mapper.apiListToEntityList(apiList);
        assertEquals(apiList.size(), entityList.size());

        RecommendationEntity entity = entityList.get(0);

        assertEquals(api.productId(), entity.productId());
        assertEquals(api.recommendationId(), entity.recommendationId());
        assertEquals(api.author(), entity.author());
        assertEquals(api.rate(), entity.rating());
        assertEquals(api.content(), entity.content());

        List<Recommendation> api2List = mapper.entityListToApiList(entityList);
        assertEquals(apiList.size(), api2List.size());

        Recommendation api2 = api2List.get(0);

        assertEquals(api.productId(), api2.productId());
        assertEquals(api.recommendationId(), api2.recommendationId());
        assertEquals(api.author(), api2.author());
        assertEquals(api.rate(), api2.rate());
        assertEquals(api.content(), api2.content());
        assertNull(api2.serviceAddress());
    }
}
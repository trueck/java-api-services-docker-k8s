package com.example.recommendation;


import com.example.recommendation.persistence.RecommendationEntity;
import com.example.recommendation.persistence.RecommendationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;


@DataMongoTest
@ExtendWith(SpringExtension.class)
public class PersistenceTests {

    @Autowired
    private RecommendationRepository repository;

    private RecommendationEntity savedEntity;

    @BeforeEach
    public void setupDb() {
        repository.deleteAll().block();

        RecommendationEntity entity = new RecommendationEntity(null, null, 1, 2, "a", 3, "c");
        savedEntity = repository.save(entity).block();

        assertEqualsRecommendation(entity, savedEntity);
    }


    @Test
    public void create() {

        RecommendationEntity newEntity = new RecommendationEntity(null, null, 1, 3, "a", 3, "c");
        newEntity = repository.save(newEntity).block();

        RecommendationEntity foundEntity = repository.findById(newEntity.id()).block();
        assertEqualsRecommendation(newEntity, foundEntity);

        assertEquals(2, (long)repository.count().block());
    }

    @Test
    public void update() {
        RecommendationEntity updatedRecommendation = new RecommendationEntity(savedEntity.id(), savedEntity.version(),
                savedEntity.productId(), savedEntity.recommendationId(), "a2", savedEntity.rating(), savedEntity.content());

        repository.save(updatedRecommendation).block();

        RecommendationEntity foundEntity = repository.findById(savedEntity.id()).block();
        assertEquals(1, (long)foundEntity.version());
        assertEquals("a2", foundEntity.author());
    }

    @Test
    public void delete() {
        repository.delete(savedEntity).block();
        assertFalse(repository.existsById(savedEntity.id()).block());
    }

    @Test
    public void getByProductId() {
        List<RecommendationEntity> entityList = repository.findByProductId(savedEntity.productId()).collectList().block();

        assertThat(entityList, hasSize(1));
        assertEqualsRecommendation(savedEntity, entityList.get(0));
    }

    @Test()
    public void duplicateError() {
        assertThrows(DuplicateKeyException.class,
                ()->{
                    RecommendationEntity entity = new RecommendationEntity(null, null,1, 2, "a", 3, "c");
                    repository.save(entity).block();
                });
    }

    @Test
    public void optimisticLockError() {

        // Store the saved entity in two separate entity objects
        RecommendationEntity entity1 = repository.findById(savedEntity.id()).block();
        RecommendationEntity entity2 = repository.findById(savedEntity.id()).block();



        // Update the entity using the first entity object
        RecommendationEntity updatedRecommendation1 = new RecommendationEntity(entity1.id(), entity1.version(),
                entity1.productId(), entity1.recommendationId(), "a1", entity1.rating(), entity1.content());

        repository.save(updatedRecommendation1).block();

        //  Update the entity using the second entity object.
        // This should fail since the second entity now holds a old version number, i.e. a Optimistic Lock Error
        try {
            RecommendationEntity updatedRecommendation2 = new RecommendationEntity(entity2.id(), entity2.version(),
                    entity2.productId(), entity2.recommendationId(), "a2", entity2.rating(), entity2.content());


            repository.save(updatedRecommendation2).block();

            fail("Expected an OptimisticLockingFailureException");
        } catch (OptimisticLockingFailureException e) {}

        // Get the updated entity from the database and verify its new sate
        RecommendationEntity updatedEntity = repository.findById(savedEntity.id()).block();
        Assertions.assertEquals(1, (int)updatedEntity.version());
        Assertions.assertEquals("a1", updatedEntity.author());
    }

    private void assertEqualsRecommendation(RecommendationEntity expectedEntity, RecommendationEntity actualEntity) {
        Assertions.assertNotNull(actualEntity.id());
        Assertions.assertNotNull(actualEntity.version());
        Assertions.assertEquals(expectedEntity.productId(), actualEntity.productId());
        Assertions.assertEquals(expectedEntity.recommendationId(), actualEntity.recommendationId());
        Assertions.assertEquals(expectedEntity.author(), actualEntity.author());
        Assertions.assertEquals(expectedEntity.rating(), actualEntity.rating());
        Assertions.assertEquals(expectedEntity.content(), actualEntity.content());
    }
}
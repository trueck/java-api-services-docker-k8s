package com.example.product.persistence;


import com.example.product.services.ReservationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;


@DataMongoTest
@RunWith(SpringRunner.class)
public class PersistenceTests {

    @Autowired
    private ProductRepository repository;

    private ProductEntity savedEntity;

    @MockBean
    private ReservationService reservationService;

    @Before
    public void setupDb() {
        StepVerifier.create(repository.deleteAll()).verifyComplete();

        ProductEntity entity = new ProductEntity(null , null, 1, "n", 1);
        StepVerifier.create(repository.save(entity))
                .expectNextMatches(createdEntity -> {
                    savedEntity = createdEntity;
                    return areProductEqual(entity, savedEntity);
                })
                .verifyComplete();
    }


    @Test
    public void create() {

        ProductEntity newEntity = new ProductEntity(null, null, 2, "n", 2);


        StepVerifier.create(repository.save(newEntity))
                .expectNextMatches(createdEntity -> newEntity.productId() == createdEntity.productId())
                .verifyComplete();

        StepVerifier.create(repository.findByProductId(2))
                .expectNextMatches(foundEntity -> areProductEqual(newEntity, foundEntity))
                .verifyComplete();

        StepVerifier.create(repository.count()).expectNext(2l).verifyComplete();
    }

    @Test
    public void update() {
        ProductEntity updatedProductEntity = new ProductEntity(savedEntity.id(), savedEntity.version(), savedEntity.productId(), "n2", savedEntity.weight());

        //savedEntity.name("n2");
        StepVerifier.create(repository.save(updatedProductEntity))
                .expectNextMatches(updatedEntity -> updatedEntity.name().equals("n2"))
                .verifyComplete();

        StepVerifier.create(repository.findById(updatedProductEntity.id()))
                .expectNextMatches(foundEntity ->
                        foundEntity.version() == 1 &&
                                foundEntity.name().equals("n2"))
                .verifyComplete();
    }

    @Test
    public void delete() {
        StepVerifier.create(repository.delete(savedEntity)).verifyComplete();
        StepVerifier.create(repository.existsById(savedEntity.id())).expectNext(false).verifyComplete();
    }

    @Test
    public void getByProductId() {
        StepVerifier.create(repository.findByProductId(savedEntity.productId()))
                .expectNextMatches(foundEntity -> areProductEqual(savedEntity, foundEntity))
                .verifyComplete();
    }

    @Test()
    public void duplicateError() {
        ProductEntity entity = new ProductEntity(null, null, savedEntity.productId(), "n", 1);
        StepVerifier.create(repository.save(entity)).expectError(DuplicateKeyException.class).verify();
    }

    @Test
    public void optimisticLockError() {

        // Store the saved entity in two separate entity objects
        ProductEntity entity1 = repository.findById(savedEntity.id()).block();
        ProductEntity entity2 = repository.findById(savedEntity.id()).block();

        // Update the entity using the first entity object
       // entity1.name("n1");
        ProductEntity updatedProductEntity = new ProductEntity(entity1.id(), entity1.version(), entity1.productId(), "n1", entity1.weight());
        repository.save(updatedProductEntity).block();

        //  Update the entity using the second entity object.
        // This should fail since the second entity now holds a old version number, i.e. a Optimistic Lock Error
        StepVerifier.create(repository.save(entity2)).expectError(OptimisticLockingFailureException.class).verify();

        // Get the updated entity from the database and verify its new sate
        StepVerifier.create(repository.findById(savedEntity.id()))
                .expectNextMatches(foundEntity ->
                        foundEntity.version() == 1 &&
                                foundEntity.name().equals("n1"))
                .verifyComplete();
    }

    private boolean areProductEqual(ProductEntity expectedEntity, ProductEntity actualEntity) {
        return
                actualEntity.id() != null && actualEntity.version() != null
                &&
//                (expectedEntity.id().equals(actualEntity.id())) &&
//                        (expectedEntity.version() == actualEntity.version()) &&
                        (expectedEntity.productId() == actualEntity.productId()) &&
                        (expectedEntity.name().equals(actualEntity.name())) &&
                        (expectedEntity.weight() == actualEntity.weight());
    }
}
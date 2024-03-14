package com.example.product;

import com.example.product.persistence.Reservation;

import com.example.product.services.ReservationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
public class ReservationEntityTest {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @MockBean
    private ReservationService reservationService;

    @Test
    public void persist(){
        Reservation reservation = new Reservation(null, "Edwin");
        Mono<Reservation> saved = this.reactiveMongoTemplate.save(reservation);

        StepVerifier.create(Flux.just("1", "2", "3")).expectNext("1")
                .expectNext("2")
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(Flux.just("1", "2", "3"))
                .expectNextMatches(e -> List.of("1", "2", "3").contains(e))
                .expectNextCount(2)
                .verifyComplete();

        StepVerifier.create(saved)
                .expectNextMatches(e -> e.getName().equals("Edwin") && e.getId() != null)
                .verifyComplete();
    }
}

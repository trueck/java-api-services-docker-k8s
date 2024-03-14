package com.example.product;

import com.example.product.persistence.Reservation;
import com.example.product.persistence.ReservationRepository;

import com.example.product.services.ReservationService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DataMongoTest
@RunWith(SpringRunner.class)
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @MockBean
    private ReservationService reservationService;

    @Test
    public void persist(){

        StepVerifier.create(this.reservationRepository.deleteAll())
                .verifyComplete();


        Flux<Reservation> saved = this.reservationRepository.saveAll(
                Flux.just(
                        new Reservation(null, "Edwin"),
                        new Reservation(null, "Benny"),
                        new Reservation(null, "Johnny"),
                        new Reservation(null, "Edwin")
                )
        );
        StepVerifier.create(saved).expectNextCount(4).verifyComplete();

        Flux<Reservation> find = this.reservationRepository.findByName("Edwin");

        StepVerifier.create(find).expectNextCount(2).verifyComplete();
    }
}

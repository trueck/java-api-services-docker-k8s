package com.example.product.persistence;

import com.example.product.persistence.Reservation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ReservationRepository extends ReactiveCrudRepository<Reservation, String> {
    Flux<Reservation> findByName(String name);
}

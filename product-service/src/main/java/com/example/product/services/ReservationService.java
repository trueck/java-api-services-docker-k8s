package com.example.product.services;

import com.example.product.persistence.Reservation;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


@Component
public class ReservationService {

    private final WebClient webClient;

    public ReservationService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public Flux<Reservation> getNames(){
        return webClient.get()
                .uri("http://localhost:8080/names")
                .retrieve()
                .bodyToFlux(Reservation.class);
    }
}

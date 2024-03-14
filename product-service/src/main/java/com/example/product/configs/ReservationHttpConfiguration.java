package com.example.product.configs;

import com.example.product.persistence.Reservation;
import com.example.product.persistence.ReservationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ReservationHttpConfiguration {

    @Bean
    RouterFunction<ServerResponse> routes(ReservationRepository reservationRepository) {

        return route()
                .GET("/reservations", request -> ServerResponse.ok().body(reservationRepository.findAll(), Reservation.class))
                .build();
    }
}

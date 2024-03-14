package com.example.product;

import com.example.product.persistence.Reservation;
import com.example.product.services.ReservationService;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest
@AutoConfigureWireMock
public class WebClientTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    void contextLoads(){

        WireMock.stubFor(
                WireMock.get(WireMock.urlEqualTo("/names"))
                        .willReturn(aResponse()
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                .withBody("""
                                {"id":1,"name":"Edwin"}
                                """)
                                .withStatus(HttpStatus.OK.value())
                        )
        );


        Flux<Reservation> reservations = reservationService.getNames();

        StepVerifier.create(reservations)
                .expectNextMatches(e -> e.getId() != null && e.getName().equals("Edwin"))
                .verifyComplete();
    }
}

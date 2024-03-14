package com.example.product;

import com.example.product.configs.ReservationHttpConfiguration;
import com.example.product.persistence.ProductRepository;
import com.example.product.persistence.Reservation;
import com.example.product.persistence.ReservationRepository;
import com.example.product.services.ReservationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@WebFluxTest
//@RunWith(SpringRunner.class)
@Import(ReservationHttpConfiguration.class)
@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment=RANDOM_PORT)
public class ReservationHttpTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ReservationRepository reservationRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private SampleDataInitializer sampleDataInitializer;

    @MockBean
    private ReservationService reservationService;

    @Before
    public void setup(){
        Mockito.when(this.reservationRepository.findAll())
                .thenReturn(Flux.just(new Reservation(null, "Edwin"),
                        new Reservation(null, "Benny"),
                        new Reservation(null, "Johnny"),
                        new Reservation(null, "Edwin")));

        Mockito.when(this.reservationRepository.deleteAll())
                .thenReturn(Mono.empty());
    }

    @Test
    public void getAllReservations(){

        this.webTestClient.get()
                .uri("/reservations")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("@.[0].name").isEqualTo("Edwin")
        ;
    }
}

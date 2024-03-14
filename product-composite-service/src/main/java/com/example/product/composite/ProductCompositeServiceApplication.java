package com.example.product.composite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@ComponentScan("com.example")
public class ProductCompositeServiceApplication {

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(ProductCompositeServiceApplication.class, args);
	}

	@Bean
	RouterFunction<ServerResponse> routes(GreetingService greetingService){
		return route()
				.GET("/greeting/{name}", request -> {
                    GreetingRequest greetingRequest = new GreetingRequest(request.pathVariable("name"));
                    Mono<GreetingResponse> greeting = greetingService.greet(greetingRequest);
                    return ServerResponse.ok().body(greeting, GreetingResponse.class);
                })
				.GET("/greetingMany/{name}", request -> {
					GreetingRequest greetingRequest = new GreetingRequest(request.pathVariable("name"));
					Flux<GreetingResponse> greeting = greetingService.greetMany(greetingRequest);
					return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(greeting, GreetingResponse.class);
				})
				.build();
	}
}
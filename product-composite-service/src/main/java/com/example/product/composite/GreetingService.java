package com.example.product.composite;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.stream.Stream;

@Service
public class GreetingService {
    Mono<GreetingResponse> greet(GreetingRequest greetingRequest){
        return Mono.just(new GreetingResponse("hello " + greetingRequest.getName()));
    }

    Flux<GreetingResponse> greetMany(GreetingRequest greetingRequest){
        return Flux.fromStream(Stream.generate(()-> new GreetingResponse("hello " + greetingRequest.getName())))
                .delayElements(Duration.ofSeconds(1));
    }
}

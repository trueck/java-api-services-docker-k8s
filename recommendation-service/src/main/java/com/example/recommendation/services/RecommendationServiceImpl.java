package com.example.recommendation.services;

import com.example.api.core.recommendation.Recommendation;
import com.example.api.core.recommendation.RecommendationService;
import com.example.recommendation.persistence.RecommendationEntity;
import com.example.recommendation.persistence.RecommendationRepository;
import com.example.util.exceptions.InvalidInputException;
import com.example.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class RecommendationServiceImpl implements RecommendationService {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private final RecommendationRepository repository;

    private final RecommendationMapper mapper;

    private final ServiceUtil serviceUtil;

    @Autowired
    public RecommendationServiceImpl(RecommendationRepository repository, RecommendationMapper mapper, ServiceUtil serviceUtil) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Mono<Recommendation> createRecommendation(Recommendation body) {
        if (body.getProductId() < 1) throw new InvalidInputException("Invalid productId: " + body.getProductId());

        RecommendationEntity entity = mapper.apiToEntity(body);
        Mono<Recommendation> newEntity = repository.save(entity)
                .log()
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", Recommendation Id:" + body.getRecommendationId()))
                .map(e -> mapper.entityToApi(e));

        return newEntity;
    }

    @Override
    public Flux<Recommendation> getRecommendations(int productId) {

        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

        return repository.findByProductId(productId)
                .log()
                .map(e -> mapper.entityToApi(e))
                .map(e -> {e.setServiceAddress(serviceUtil.getServiceAddress()); return e;});
    }

    @Override
    public Mono<Void> deleteRecommendations(int productId) {
        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

        LOG.debug("deleteRecommendations: tries to delete recommendations for the product with productId: {}", productId);
        return repository.deleteAll(repository.findByProductId(productId));
    }
}
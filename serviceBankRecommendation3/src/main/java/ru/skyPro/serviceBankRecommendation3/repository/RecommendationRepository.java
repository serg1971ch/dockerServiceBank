package ru.skyPro.serviceBankRecommendation3.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.skyPro.serviceBankRecommendation3.model.rulesEntity.Recommendation;

public interface RecommendationRepository extends ListCrudRepository<Recommendation, Long> {
    Recommendation findByProductName(String productName);
}

package ru.skyPro.serviceBankRecommendation3.service;

import org.springframework.stereotype.Service;
import ru.skyPro.serviceBankRecommendation3.model.BankRecommendationRule;
import ru.skyPro.serviceBankRecommendation3.model.ClientRecommendation;
import ru.skyPro.serviceBankRecommendation3.model.rulesEntity.Recommendation;
import ru.skyPro.serviceBankRecommendation3.model.rulesEntity.Rule;


import java.util.List;
import java.util.UUID;
@Service
public interface RecommendationService {
    ClientRecommendation getClientRecommendationByJDBCTemplate(UUID user);

    void setRecommendationRules();

    BankRecommendationRule createDynamicRule(Rule rule, Recommendation recommendation);

    List<BankRecommendationRule> getAllRules();

    void removeRule(int id);

    BankRecommendationRule getRuleByUserId(UUID id);

}

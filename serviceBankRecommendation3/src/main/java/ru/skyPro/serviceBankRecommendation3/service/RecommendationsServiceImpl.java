package ru.skyPro.serviceBankRecommendation3.service;

import lombok.Getter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.skyPro.serviceBankRecommendation3.configuration.AppProperties;
import ru.skyPro.serviceBankRecommendation3.dto.RuleDto;
import ru.skyPro.serviceBankRecommendation3.exceptions.RecommendBankException;
import ru.skyPro.serviceBankRecommendation3.model.BankRecommendationRule;
import ru.skyPro.serviceBankRecommendation3.model.ClientRecommendation;
import ru.skyPro.serviceBankRecommendation3.model.rulesEntity.Recommendation;
import ru.skyPro.serviceBankRecommendation3.model.rulesEntity.Rule;
import ru.skyPro.serviceBankRecommendation3.repository.RecommendationRepository;
import ru.skyPro.serviceBankRecommendation3.repository.RecommendationsRepository;
import ru.skyPro.serviceBankRecommendation3.repository.RuleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationsServiceImpl implements RecommendationService {
    private final RuleRepository ruleRepository;
    private final CombinerQueryService combinerService;
    private final AppProperties appProperties;
    private final RecommendationsRepository recommendationsRepository;
    private final RecommendationRepository recommendationRepository;
    private final JdbcTemplate jdbcTemplate;
    @Getter
    private List<BankRecommendationRule> recommendationsList;

    public RecommendationsServiceImpl(RuleRepository ruleRepository, CombinerQueryService combinerService, AppProperties appProperties,
                                      RecommendationsRepository recommendationsRepository, RecommendationRepository recommendationRepository, JdbcTemplate jdbcTemplate, List<BankRecommendationRule> recommendationsList) {
        this.ruleRepository = ruleRepository;
        this.combinerService = combinerService;
        this.appProperties = appProperties;
        this.recommendationsRepository = recommendationsRepository;
        this.recommendationRepository = recommendationRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.recommendationsList = recommendationsList;
    }

    @Override
    public ClientRecommendation getClientRecommendationByJDBCTemplate(UUID userId) {
        ClientRecommendation client;
        List<String> listProducts = recommendationsRepository.getRecommenationForUserFirstMethod(userId);
        List<BankRecommendationRule> bankServiceRecommendations = new ArrayList<>();

        if (listProducts.contains(appProperties.getNameOne())) {
            bankServiceRecommendations.add(new BankRecommendationRule(UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a"), appProperties.getNameOne(), appProperties.getDescriptionOne()));
            client = new ClientRecommendation(userId, bankServiceRecommendations);
            return client;
        } else if (listProducts.contains(appProperties.getNameTwo())) {
            bankServiceRecommendations.add(new BankRecommendationRule(UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925"), appProperties.getNameTwo(), appProperties.getDescriptionTwo()));
            client = new ClientRecommendation(userId, bankServiceRecommendations);
            return client;
        } else if (listProducts.contains(appProperties.getNameThree())) {
            bankServiceRecommendations.add(new BankRecommendationRule(UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f"), appProperties.getNameThree(), appProperties.getDescriptionThree()));
            client = new ClientRecommendation(userId, bankServiceRecommendations);
            return client;
        } else {
            throw new RecommendBankException("recommendation not found");
        }
    }

    @Override
    public void setRecommendationRules() {
        BankRecommendationRule ruleRecommendationOne, ruleRecommendationTwo, ruleRecommendationThree;
        Recommendation recommendationOne = recommendationRepository.findById(1L).orElseThrow(() -> new RecommendBankException("recommendation not found"));
        Recommendation recommendationTwo = recommendationRepository.findById(2L).orElseThrow(() -> new RecommendBankException("recommendation not found"));
        Recommendation recommendationThree = recommendationRepository.findById(3L).orElseThrow(() -> new RecommendBankException("recommendation not found"));
        ruleRecommendationOne = combinerService.getRecommendation(recommendationOne.getId());
        ruleRecommendationTwo = combinerService.getRecommendation(recommendationTwo.getId());
        ruleRecommendationThree = combinerService.getRecommendation(recommendationThree.getId());
        recommendationsList = List.of(new BankRecommendationRule[]{
                ruleRecommendationOne,
                ruleRecommendationTwo,
                ruleRecommendationThree
        });
    }

    @Override
    public BankRecommendationRule createDynamicRule(Rule newRule, Recommendation recommendation) {
        newRule = new Rule();
        ruleRepository.save(newRule);
        recommendation = recommendationRepository.findById(recommendation.getId()).orElseThrow(() -> new RecommendBankException("recommendation not found"));
        BankRecommendationRule recommendationRule = combinerService.getRecommendation(recommendation.getId());
        List<RuleDto> rulesOfRecommendation = recommendationRule.getRules();
        rulesOfRecommendation.add(combinerService.getStringArgumentsOfRule(newRule));
        return recommendationRule;
    }

    @Override
    public List<BankRecommendationRule> getAllRules() {
        setRecommendationRules();
        return recommendationsList;
    }

    @Override
    public void removeRule(int id) {
        recommendationsList.remove(id);
    }

    @Override
    public BankRecommendationRule getRuleByUserId(UUID id) {
        String startQuery = "SELECT CASE ";
        String finishQuery = "END AS result FROM PRODUCTS p JOIN TRANSACTIONS t ON t.product_id = p.id " +
                "WHERE t.user_id = ?";
        String queryOne = startQuery + recommendationsList.get(0).getRules().get(0).getQuery();
        String queryTwo = startQuery + recommendationsList.get(0).getRules().get(1).getQuery();
        String queryThree = startQuery + recommendationsList.get(0).getRules().get(2).getQuery();
        String queryOne1 = startQuery + recommendationsList.get(1).getRules().get(0).getQuery();
        String queryTwo1 = startQuery + recommendationsList.get(1).getRules().get(1).getQuery();
        String queryThree1 = startQuery + recommendationsList.get(1).getRules().get(2).getQuery();
        StringBuilder builder = new StringBuilder();
        builder.append(startQuery).append(queryOne).append(queryTwo).append(queryThree)
                .append("THEN 'Invest500' ")
                .append(queryOne1).append(queryTwo1).append(queryThree1)
                .append("THEN 'Simple Credit' ELSE '0' END AS result ")
                .append(finishQuery);

        List<String> sqlRecommendation = jdbcTemplate.queryForList(String.valueOf(builder), String.class, id);
        String sqlResponse = sqlRecommendation.get(0);

        return switch (sqlResponse) {
            case "Invest500" -> recommendationsList.get(0);
            case "Simple Credit" -> recommendationsList.get(1);
            default -> throw new RecommendBankException("recommendation not found");
        };
    }
}


package ru.skyPro.serviceBankRecommendation3.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skyPro.serviceBankRecommendation3.dto.RuleDto;
import ru.skyPro.serviceBankRecommendation3.exceptions.RecommendBankException;
import ru.skyPro.serviceBankRecommendation3.model.BankRecommendationRule;
import ru.skyPro.serviceBankRecommendation3.model.rulesEntity.Recommendation;
import ru.skyPro.serviceBankRecommendation3.model.rulesEntity.Rule;
import ru.skyPro.serviceBankRecommendation3.repository.RecommendationRepository;
import ru.skyPro.serviceBankRecommendation3.repository.RuleRepository;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.skyPro.serviceBankRecommendation3.util.ConstantQueriesOperation.*;


@Service
public class CombinerQueryService {

    private final RuleRepository ruleRepository;
    private final RecommendationRepository recommendationRepository;
    private static final Logger log = LoggerFactory.getLogger(CombinerQueryService.class);


    @Autowired
    public CombinerQueryService(RuleRepository ruleRepository, RecommendationRepository recommendationRepository) {
        this.ruleRepository = ruleRepository;
        this.recommendationRepository = recommendationRepository;
    }

    public Rule setQueryUserOfProduct() {
        Rule rule = ruleRepository.findByQueryName("USER_OF");
        DEBIT = new String(rule.getArguments(), StandardCharsets.UTF_8);
        rule.setQuery("SUM(CASE WHEN p.type  " + (rule.isNegate() ? "!=" : "=") +
                DEBIT.replaceAll("[{}\"]", "").trim()  +
                  " THEN 1 ELSE 0 END) > 0 ");
        return ruleRepository.save(rule);
    }

    public Rule setQueryActiveUserOfProduct() {
        Rule rule = ruleRepository.findByQueryName(ACTIVE);
        String DEBIT = new String(rule.getArguments(), StandardCharsets.UTF_8);
        rule.setQuery("SUM(CASE WHEN p.type " + (rule.isNegate() ? "!=" : "=") + DEBIT.replaceAll("[{}\"]", "").trim() + "THEN 1 ELSE 0 END) > 5 ");
        return ruleRepository.save(rule);
    }

    public Rule setQueryTransactionSummaryCompare() {
        Rule rule = ruleRepository.findByQueryName(TRANSACTION_SUM);
        String compareArgs = new String(rule.getArguments(), StandardCharsets.UTF_8);
        String[] splitArgs = compareArgs.split(",");
        rule.setQuery("SUM(CASE WHEN p.type " +
                (rule.isNegate() ? "!=" : "=") + splitArgs[0].replaceAll("[{}\"]", "").trim() + " THEN t.amount" +
                (rule.isNegate() ? "!=" : "=") + splitArgs[1].replaceAll("[{}\"]", "").trim() + " ELSE 0 END) " + splitArgs[2].replaceAll("[{}\"]", "").trim() + splitArgs[3].replaceAll("[{}\"]", "").trim());
        return ruleRepository.save(rule);
    }

    public Rule setQueryForUserInvest() {
        Rule rule = ruleRepository.findByQueryName(USER_FOR_INVEST);
        String compareTransactionArgs = new String(rule.getArguments(), StandardCharsets.UTF_8);
        String[] transactionArgs = compareTransactionArgs.split(",");
        String invest = transactionArgs[0].replaceAll("[{}\"]", "").trim();
        rule.setQuery("SUM(CASE WHEN p.type " + (rule.isNegate() ? "!=" : "=" ) + invest + " THEN 1 ELSE 0 END) > 0 ");
        return ruleRepository.save(rule);
    }

    public Rule setQueryTransactionSumCompareDepositWithdraw() {
        Rule rule = ruleRepository.findByQueryName(TRANSACTION_SUM_COMPARE);
        String compareTransactionSumArgs = new String(rule.getArguments(), StandardCharsets.UTF_8);
        String[] transactionSumArgs = compareTransactionSumArgs.split(",");
        rule.setQuery("SUM(CASE WHEN p.type " +
                (rule.isNegate() ? "!=" : "=") + transactionSumArgs[0].trim() +
                " THEN t.amount = 'DEPOSIT'  ELSE 0 END) " + transactionSumArgs[1].replaceAll("[{}\"]", "").trim() +
                " SUM(CASE WHEN p.type = WITHDRAW  THEN t.amount = 'WITHDRAW' ELSE 0 END) ");
        return ruleRepository.save(rule);
    }

//    public List<Rule> createCreditRule() {
//        return getRules(3L);
//    }
//
//    public List<Rule> createInvest500Rule() {
//        return getRules(1L);
//    }

    public List<Rule> getRules(Long recommendId) {
        List<Rule> dynamicRuleList = new ArrayList<>();
        Rule firstRule = setQueryUserOfProduct();
        Rule secondRule = setQueryActiveUserOfProduct();
        Rule thirdRule = setQueryTransactionSummaryCompare();
        Rule thourthRule = setQueryForUserInvest();
        Recommendation recommendation = recommendationRepository.findById(recommendId)
                .orElseThrow(() -> new RecommendBankException("recommendation not found"));
        if (recommendation.getId() == 1L) {
            dynamicRuleList.add(firstRule);
            dynamicRuleList.add(secondRule);
            dynamicRuleList.add(thourthRule);
            Recommendation finalRecommendation = recommendation;
            dynamicRuleList.forEach(rule -> {
                rule.setRecommendation(finalRecommendation);
            });
            return dynamicRuleList;
//            recommendation.setRules(dynamicRuleList);
//            ruleRepository.save(firstRule);
//            ruleRepository.save(secondRule);
//            ruleRepository.save(thourthRule);
        }
//        if (recommendation.getId() == 2L) {
//            dynamicRuleList.add(firstRule);
//            dynamicRuleList.add(secondRule);
//            dynamicRuleList.add(thirdRule);
//            rule.setRecommendation(recommendation);
//            ruleRepository.save(rule);
//        }
        if (recommendation.getId() == 3L) {
            dynamicRuleList.add(firstRule);
            dynamicRuleList.add(secondRule);
            dynamicRuleList.add(thirdRule);
            Recommendation finalRecommendation1 = recommendation;
            dynamicRuleList.forEach(rule -> {
                rule.setRecommendation(finalRecommendation1);
            });
            return dynamicRuleList;
        }
        log.info("create Credit rule is: {}", dynamicRuleList.toString());
        return dynamicRuleList;
    }

    // Метод обновления Recommendation и связанных правил
    public BankRecommendationRule getRecommendation(Long id) {
        Recommendation recommendation = recommendationRepository.findById(id).orElseThrow(() -> new RecommendBankException("recommendation not found"));
        List<Rule> currentList = getRules(id);
        List<RuleDto> newList = currentList.stream().map(this::getStringArgumentsOfRule).toList();
        log.info("this is recommendation has list of Rules: {}", currentList.stream().toList());
        return new BankRecommendationRule(UUID.randomUUID(),
                recommendation.getProductName(), recommendation.getDescription(), newList);
    }

    public RuleDto getStringArgumentsOfRule(Rule rule) {
        String arguments = new String(rule.getArguments(), StandardCharsets.UTF_8);
        String[] splitArgs = arguments.split(",");

        for (int i = 0; i < splitArgs.length; i++) {
            splitArgs[i] = splitArgs[i].replaceAll("[{}\"]", "").trim();
        }
        return new RuleDto(rule.getQueryName(), splitArgs, rule.isNegate());
    }

    private void convertToStringArgs(String args) {
        args.replaceAll("[{}\"]", "").trim();
    }
}



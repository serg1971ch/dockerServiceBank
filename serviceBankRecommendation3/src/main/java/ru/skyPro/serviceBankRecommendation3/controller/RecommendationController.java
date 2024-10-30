package ru.skyPro.serviceBankRecommendation3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skyPro.serviceBankRecommendation3.model.BankRecommendationRule;
import ru.skyPro.serviceBankRecommendation3.model.rulesEntity.Recommendation;
import ru.skyPro.serviceBankRecommendation3.model.rulesEntity.Rule;
import ru.skyPro.serviceBankRecommendation3.service.CombinerQueryService;
import ru.skyPro.serviceBankRecommendation3.service.RecommendationService;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService service;
    private final CombinerQueryService queryService;

    @Autowired
    public RecommendationController(RecommendationService service, CombinerQueryService queryService) {
        this.service = service;
        this.queryService = queryService;
    }

    @PostMapping("/rule")
    public ResponseEntity<BankRecommendationRule> addRecommendation(@RequestBody Rule rule,
                                                                    @RequestBody Recommendation recommendation) {
        return ResponseEntity.ok(queryService.getRecommendation(recommendation.getId()));
    }

    @GetMapping("/rule")
    public List<BankRecommendationRule> getAllRecommendations() {
        return service.getAllRules();
    }

    @DeleteMapping("/rule/{ruleId}")
    public void removeRecommendation(@PathVariable int ruleId) {
        service.setRecommendationRules();
        service.removeRule(ruleId);
    }

    @GetMapping("/user/{id}")
    public BankRecommendationRule getRecommendation(@PathVariable String id) {
        return service.getRuleByUserId(UUID.fromString(id));
    }
}

package ru.skyPro.serviceBankRecommendation3.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.skyPro.serviceBankRecommendation3.dto.RuleDto;
import ru.skyPro.serviceBankRecommendation3.model.rulesEntity.Rule;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class BankRecommendationRule {

    private UUID id;
    private String productName;
    private String description;
    private List<RuleDto> rules;

    public BankRecommendationRule(UUID id, String productName, String description) {
        this.id = id;
        this.productName = productName;
        this.description = description;
    }

    public BankRecommendationRule(UUID id, String productName, String description, List<RuleDto> rules) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.rules = rules;
    }

    @Override
    public String toString() {
        return "Клиенту рекомендуется: " + productName + " -  " + description;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public UUID getId() {
        return id;
    }

    public void setRules(List<RuleDto> rules) {
        this.rules = rules;
    }

    public List<RuleDto> getRules() {
        return rules;
    }
}

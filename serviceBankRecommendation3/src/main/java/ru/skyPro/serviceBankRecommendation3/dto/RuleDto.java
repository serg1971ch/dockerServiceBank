package ru.skyPro.serviceBankRecommendation3.dto;

import lombok.Data;


@Data
public class RuleDto {
    private String query;
    private String[] arguments;
    private boolean negate;


    public RuleDto(String query, String[] arguments, boolean negate) {
        this.query = query;
        this.arguments = arguments;
        this.negate = negate;
    }
}

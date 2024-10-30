package ru.skyPro.serviceBankRecommendation3.exceptions;

public class RecommendBankException extends RuntimeException {
    public RecommendBankException(String recommendationNotFound) {
        super("That user does not have recommend from bank");
    }
}

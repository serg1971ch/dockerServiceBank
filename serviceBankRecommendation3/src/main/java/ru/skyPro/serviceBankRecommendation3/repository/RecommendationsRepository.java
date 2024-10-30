package ru.skyPro.serviceBankRecommendation3.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> getRecommenationForUserFirstMethod(UUID user) {
        String sql = "SELECT " +
                "CASE " +
                "WHEN " +
                "SUM(CASE WHEN p.type = 'DEBIT' THEN 1 ELSE 0 END) > 0 " +
                "AND SUM(CASE WHEN p.type = 'INVEST' THEN 1 ELSE 0 END) = 0 " +
                "AND SUM(CASE WHEN p.type = 'SAVING' AND t.amount > 0 THEN t.amount ELSE 0 END) > 1000 " +
                "THEN 'Invest500' " +
                "WHEN " +
                "SUM(CASE WHEN p.type = 'DEBIT' THEN 1 ELSE 0 END) > 0 " +
                "AND (SUM(CASE WHEN p.type = 'DEBIT' THEN t.amount ELSE 0 END) >= 50000 " +
                "OR SUM(CASE WHEN p.type = 'SAVING' THEN t.amount ELSE 0 END) >= 50000) " +
                "AND SUM(CASE WHEN p.type = 'DEBIT' THEN t.amount ELSE 0 END) > SUM(CASE WHEN p.type = 'WITHDRAW' THEN t.amount ELSE 0 END) " +
                "THEN 'Top Saving'" +
                "WHEN " +
                "SUM(CASE WHEN p.type = 'CREDIT' THEN 1 ELSE 0 END) = 0 " +
                "AND SUM(CASE WHEN t.type = 'DEPOSIT' THEN t.amount ELSE 0 END) > SUM(CASE WHEN p.type = 'WITHDRAW' THEN t.amount ELSE 0 END) " +
                "AND SUM(CASE WHEN p.type = 'DEBIT' THEN t.amount ELSE 0 END) > 100000 " +
                "THEN 'Simple Credit' " +
                "ELSE '0' " +
                "END AS result " +
                "FROM PRODUCTS p " +
                "JOIN TRANSACTIONS t ON t.product_id = p.id " +
                "WHERE t.user_id = ?";
        return jdbcTemplate.queryForList(sql, String.class, user);
    }
}

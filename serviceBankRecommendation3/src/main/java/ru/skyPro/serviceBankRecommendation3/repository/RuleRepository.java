package ru.skyPro.serviceBankRecommendation3.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skyPro.serviceBankRecommendation3.model.rulesEntity.Rule;


@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {
    Rule findByQueryName(String name);
}

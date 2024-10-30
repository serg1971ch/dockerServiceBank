package ru.skyPro.serviceBankRecommendation3.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skyPro.serviceBankRecommendation3.model.ClientRecommendation;
import ru.skyPro.serviceBankRecommendation3.service.RecommendationService;


import java.util.UUID;

@RestController
@RequestMapping("/client")
@Slf4j
public class RecommendationUserController {
    private final RecommendationService service;
    @Autowired
    public RecommendationUserController( RecommendationService service) {
        this.service = service;
    }

    @GetMapping("/user/{id}")
    public ClientRecommendation getRecommendationUser(@PathVariable String id) {
        log.info("запрос на рекомендацию по данному id: [{}]", id);
        return service.getClientRecommendationByJDBCTemplate(UUID.fromString(id));
    }
}

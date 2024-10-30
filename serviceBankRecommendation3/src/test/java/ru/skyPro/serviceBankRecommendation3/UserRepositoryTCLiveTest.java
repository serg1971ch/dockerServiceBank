package ru.skyPro.serviceBankRecommendation3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skyPro.serviceBankRecommendation3.controller.RecommendationUserController;
import ru.skyPro.serviceBankRecommendation3.model.ClientRecommendation;
import ru.skyPro.serviceBankRecommendation3.service.RecommendationsServiceImpl;

import java.util.UUID;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRepositoryTCLiveTest  {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RecommendationUserController controller;

    @Autowired
    private RecommendationsServiceImpl recommendationService;

@Container
static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

//    @Test
//    @Transactional
//    public void givenUsersInDB_WhenUpdateStatusForNameModifyingQueryAnnotationNative_ThenModifyMatchingUsers() {
//        userRepository.save(new User("SAMPLE", LocalDate.now(), USER_EMAIL, ACTIVE_STATUS));
//        userRepository.save(new User("SAMPLE1", LocalDate.now(), USER_EMAIL2, ACTIVE_STATUS));
//        userRepository.save(new User("SAMPLE", LocalDate.now(), USER_EMAIL3, ACTIVE_STATUS));
//        userRepository.save(new User("SAMPLE3", LocalDate.now(), USER_EMAIL4, ACTIVE_STATUS));
//        userRepository.flush();
//
//        int updatedUsersSize = userRepository.updateUserSetStatusForNameNativePostgres(INACTIVE_STATUS, "SAMPLE");
//
//        assertThat(updatedUsersSize).isEqualTo(2);
//    }


    @Test
    public void testGetRecommendationUser() {
        // Arrange
        ClientRecommendation clientRecommendation = recommendationService.getClientRecommendationByJDBCTemplate(UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d"));

        // Act
        ResponseEntity<ClientRecommendation> clientRecommendationeResponseEntity =
                restTemplate.getForEntity("http://localhost:8189"  + "/client/user/"+ clientRecommendation.getId(), ClientRecommendation.class);
        // Assert
        Assertions.assertEquals(clientRecommendationeResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

    }

//    static class Initializer
//            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
//            TestPropertyValues.of(
//                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
//                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
//                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
//            ).applyTo(configurableApplicationContext.getEnvironment());
//        }
//    }
}

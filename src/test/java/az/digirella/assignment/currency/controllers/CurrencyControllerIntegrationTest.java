package az.digirella.assignment.currency.controllers;

import az.digirella.assignment.currency.TestMsSqlContainer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ulphat
 */
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CurrencyControllerIntegrationTest {

    @Container
    private static final TestMsSqlContainer MSSQL_SERVER_CONTAINER = TestMsSqlContainer.getInstance();

    private static final String PATH_CURRENCIES = "/currencies";
    private static final String PATH_DATE = PATH_CURRENCIES + "/{date}.json";
    private static final String VALID_DATE = "25.01.2022";
    private static final String ANOTHER_VALID_DATE = "26.01.2022";
    private static final String FUTURE_DATE = "25.01.2122";
    private static final String INVALID_DATE = "2122";
    private static final String VALID_CODE = "USD";
    private static final String INVALID_CODE = "INVALID_CODE";
    private static final String AUTH_BASIC = "Basic dXNlcjoxMjM0NTY=";
    private static final String AUTH_BEARER = "Bearer 1234567890";


    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    public static void overrideContainerProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", MSSQL_SERVER_CONTAINER::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", MSSQL_SERVER_CONTAINER::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", MSSQL_SERVER_CONTAINER::getPassword);
    }


    @Test
    @Order(1)
    public void collectAndGetSuccess() throws Exception {
        mockMvc.perform(get(PATH_DATE, VALID_DATE)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BEARER))
               .andExpect(jsonPath("code", is("0000")))
               .andExpect(status().isOk());
        mockMvc.perform(get(PATH_DATE, ANOTHER_VALID_DATE)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BEARER))
               .andExpect(jsonPath("code", is("0000")))
               .andExpect(status().isOk());
        mockMvc.perform(get(PATH_DATE, "27.01.2022")
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BEARER))
               .andExpect(jsonPath("code", is("0000")))
               .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void collectAndGetExists() throws Exception {
        mockMvc.perform(get(PATH_DATE, VALID_DATE)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BEARER))
               .andExpect(jsonPath("code", is("0001")))
               .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void collectAndGetNotFound() throws Exception {
        mockMvc.perform(get(PATH_DATE, FUTURE_DATE)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BEARER))
               .andExpect(jsonPath("code", is("0002")))
               .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void collectAndGet400() throws Exception {
        mockMvc.perform(get(PATH_DATE, INVALID_DATE)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BEARER))
               .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    public void collectAndGet401() throws Exception {
        mockMvc.perform(get(PATH_DATE, FUTURE_DATE))
               .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(6)
    public void collectAndGet403() throws Exception {
        mockMvc.perform(get(PATH_DATE, FUTURE_DATE)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BASIC))
               .andExpect(status().isForbidden());
    }

    @Test
    @Order(7)
    public void deleteAndGetSuccess() throws Exception {
        mockMvc.perform(delete(PATH_DATE, VALID_DATE)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BEARER))
               .andExpect(status().isNoContent());
    }

    @Test
    @Order(8)
    public void deleteAndGet400() throws Exception {
        mockMvc.perform(delete(PATH_DATE, INVALID_DATE)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BEARER))
               .andExpect(status().isBadRequest());
    }

    @Test
    @Order(9)
    public void deleteAndGet401() throws Exception {
        mockMvc.perform(delete(PATH_DATE, INVALID_DATE))
               .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(10)
    public void deleteAndGet403() throws Exception {
        mockMvc.perform(delete(PATH_DATE, VALID_DATE)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BASIC))
               .andExpect(status().isForbidden());
    }

    @Test
    @Order(11)
    public void deleteAndGet404() throws Exception {
        mockMvc.perform(delete(PATH_DATE, FUTURE_DATE)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BEARER))
               .andExpect(status().isNotFound());
    }

    @Test
    @Order(12)
    public void getListAndExpectData() throws Exception {
        mockMvc.perform(get(PATH_CURRENCIES)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BASIC))
               .andExpect(jsonPath("content").isArray())
               .andExpect(jsonPath("content").isNotEmpty())
               .andExpect(jsonPath("totalPages", not(0)))
               .andExpect(jsonPath("totalPages", not(1)))
               .andExpect(jsonPath("totalElements", not(0)))
               .andExpect(jsonPath("numberOfElements", is(20)))
               .andExpect(status().isOk());
    }

    @Test
    @Order(13)
    public void filterListByDateAndExpectData() throws Exception {
        mockMvc.perform(get(PATH_CURRENCIES)
                                .param("date", ANOTHER_VALID_DATE)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BASIC))
               .andExpect(jsonPath("content").isArray())
               .andExpect(jsonPath("content").isNotEmpty())
               .andExpect(jsonPath("totalPages", not(0)))
               .andExpect(jsonPath("totalPages", not(1)))
               .andExpect(jsonPath("totalElements", not(0)))
               .andExpect(jsonPath("numberOfElements", is(20)))
               .andExpect(status().isOk());
    }

    @Test
    @Order(14)
    public void filterListByCodeAndExpectData() throws Exception {
        mockMvc.perform(get(PATH_CURRENCIES)
                                .param("code", VALID_CODE)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BASIC))
               .andExpect(jsonPath("content").isArray())
               .andExpect(jsonPath("content").isNotEmpty())
               .andExpect(jsonPath("totalPages", is(1)))
               .andExpect(jsonPath("totalElements", is(2)))
               .andExpect(jsonPath("numberOfElements", is(2)))
               .andExpect(status().isOk());
    }

    @Test
    @Order(15)
    public void filterListByDateAndCodeAndExpectData() throws Exception {
        mockMvc.perform(get(PATH_CURRENCIES)
                                .param("date", ANOTHER_VALID_DATE)
                                .param("code", VALID_CODE)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BASIC))
               .andExpect(jsonPath("content").isArray())
               .andExpect(jsonPath("content").isNotEmpty())
               .andExpect(jsonPath("totalPages", is(1)))
               .andExpect(jsonPath("totalElements", is(1)))
               .andExpect(jsonPath("numberOfElements", is(1)))
               .andExpect(status().isOk());
    }

    @Test
    @Order(16)
    public void filterListByDateAndExpectEmpty() throws Exception {
        mockMvc.perform(get(PATH_CURRENCIES)
                                .param("date", FUTURE_DATE)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BASIC))
               .andExpect(jsonPath("content").isArray())
               .andExpect(jsonPath("content").isEmpty())
               .andExpect(jsonPath("totalPages", is(0)))
               .andExpect(jsonPath("totalElements", is(0)))
               .andExpect(jsonPath("numberOfElements", is(0)))
               .andExpect(status().isOk());
    }

    @Test
    @Order(17)
    public void filterListByFutureDateAndCodeAndExpectEmpty() throws Exception {
        mockMvc.perform(get(PATH_CURRENCIES)
                                .param("date", FUTURE_DATE)
                                .param("code", VALID_CODE)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BASIC))
               .andExpect(jsonPath("content").isArray())
               .andExpect(jsonPath("content").isEmpty())
               .andExpect(jsonPath("totalPages", is(0)))
               .andExpect(jsonPath("totalElements", is(0)))
               .andExpect(jsonPath("numberOfElements", is(0)))
               .andExpect(status().isOk());
    }

    @Test
    @Order(18)
    public void filterListByValidDateAndInvalidCodeAndExpectEmpty() throws Exception {
        mockMvc.perform(get(PATH_CURRENCIES)
                                .param("date", VALID_DATE)
                                .param("code", INVALID_CODE)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BASIC))
               .andExpect(jsonPath("content").isArray())
               .andExpect(jsonPath("content").isEmpty())
               .andExpect(jsonPath("totalPages", is(0)))
               .andExpect(jsonPath("totalElements", is(0)))
               .andExpect(jsonPath("numberOfElements", is(0)))
               .andExpect(status().isOk());
    }

    @Test
    @Order(19)
    public void filterListByInvalidCodeAndExpectEmpty() throws Exception {
        mockMvc.perform(get(PATH_CURRENCIES)
                                .param("code", INVALID_CODE)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BASIC))
               .andExpect(jsonPath("content").isArray())
               .andExpect(jsonPath("content").isEmpty())
               .andExpect(jsonPath("totalPages", is(0)))
               .andExpect(jsonPath("totalElements", is(0)))
               .andExpect(jsonPath("numberOfElements", is(0)))
               .andExpect(status().isOk());
    }

    @Test
    @Order(20)
    public void getListAndExpect401() throws Exception {
        mockMvc.perform(get(PATH_CURRENCIES)
                                .param("code", VALID_CODE))
               .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(21)
    public void getListAndExpect403() throws Exception {
        mockMvc.perform(get(PATH_CURRENCIES)
                                .param("code", VALID_CODE)
                                .header(HttpHeaders.AUTHORIZATION, AUTH_BEARER))
               .andExpect(status().isForbidden());
    }
}

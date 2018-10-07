package io.wine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wine.controller.OrderController.WineOrder;
import io.wine.model.Orders;
import io.wine.model.User;
import io.wine.model.Wine;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

public class UserJourneyTest extends WineApplicationTests {

    private static final String USERNAME = "william";
    private static final String PASSWORD = "1234";
    @LocalServerPort
    private Integer port;

    private RestTemplate restTemplate;

    private String baseUrl;

    @Before
    public void setUp() throws Exception {
        restTemplate = new RestTemplate();
        baseUrl = "http://localhost:" + port;
    }

    @Test
    public void createOrderAndFinishOrderUserJourneyTest() {
        User user = User.builder().username(USERNAME).password(PASSWORD).build();
        createUser(user);
        String sessionId = auth(user.getUsername(), user.getPassword());
        Wine wine = Wine.builder().description("description Random").name("tinto").stock(1).build();
        int wineId = createWine(wine, sessionId);
        Integer orderId = createOrder(sessionId);
        addWine(orderId, wineId, sessionId, 1, 0);
        removeWine(orderId, wineId, sessionId, 0, 1);

    }

    private void addWine(Integer orderId, Integer wineId, String sessionId, Integer orderArraySize,
                         Integer wineStockToCompare) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("sessionId", sessionId);
        WineOrder wineOrder = WineOrder.builder().orderId(orderId).wineId(wineId).build();
        HttpEntity entity = new HttpEntity<>(wineOrder, headers);

        Orders actual = restTemplate.exchange(baseUrl + "/orders/wine/add", PUT, entity, Orders.class).getBody();

        assertEquals(orderArraySize.intValue(), actual.getWineIds().size());

        Wine wine = getWine(wineId, sessionId);

        assertEquals(wineStockToCompare, wine.getStock());
    }

    private void removeWine(Integer orderId, Integer wineId, String sessionId, Integer orderArraySize,
                         Integer wineStockToCompare) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("sessionId", sessionId);
        WineOrder wineOrder = WineOrder.builder().orderId(orderId).wineId(wineId).build();
        HttpEntity entity = new HttpEntity<>(wineOrder, headers);

        Orders actual = restTemplate.exchange(baseUrl + "/orders/wine/remove", PUT, entity, Orders.class).getBody();

        assertEquals(orderArraySize.intValue(), actual.getWineIds().size());

        Wine wine = getWine(wineId, sessionId);

        assertEquals(wineStockToCompare, wine.getStock());
    }

    private Wine getWine(Integer wineId, String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("sessionId", sessionId);
        HttpEntity entity = new HttpEntity<>(headers);

        Wine actual = restTemplate.exchange(baseUrl + "/wines" + "/" + wineId, GET, entity, Wine.class).getBody();

        assertNotNull(actual);

        return actual;
    }


    private int createWine(Wine wine, String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("sessionId", sessionId);
        HttpEntity entity = new HttpEntity<>(wine, headers);
        Wine actual = restTemplate.exchange(baseUrl + "/wines", POST, entity, Wine.class).getBody();

        assertEquals(wine.getDescription(), actual.getDescription());
        assertEquals(wine.getName(), actual.getName());
        assertEquals(wine.getStock(), actual.getStock());

        return actual.getId();
    }

    private Integer createOrder(String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("sessionId", sessionId);
        HttpEntity entity = new HttpEntity<>(headers);
        Orders actual = restTemplate.exchange(baseUrl + "/orders", POST, entity, Orders.class).getBody();

        assertNotNull(actual);

        return actual.getId();
    }

    private void createUser(User user) {
        HttpEntity entity = new HttpEntity<>(user);
        User actual = restTemplate.exchange(baseUrl + "/users", POST, entity, User.class).getBody();

        assertEquals(user.getUsername(), actual.getUsername());
        assertEquals(user.getPassword(), actual.getPassword());
    }

    private String auth(String username, String password) {
        String sessionId = restTemplate
                .exchange(baseUrl + "/users?username=" + username + "&password=" + password,
                        GET, HttpEntity.EMPTY, String.class).getBody();

        assertNotNull(sessionId);

        return sessionId;
    }

    private String convertObject(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

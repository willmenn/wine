package io.wine;

import io.wine.controller.OrderController;
import io.wine.model.Orders;
import io.wine.model.Wine;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.HttpMethod.*;

class OrderHelper {

    private RestTemplate restTemplate;
    private String baseUrl;
    private WineHelper wineHelper;

    OrderHelper(RestTemplate restTemplate, String baseUrl, WineHelper wineHelper) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.wineHelper = wineHelper;
    }

    void addWine(Integer orderId, Integer wineId, String sessionId, Integer orderArraySize,
                 Integer wineStockToCompare) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("sessionId", sessionId);
        OrderController.WineOrder wineOrder = OrderController.WineOrder.builder().orderId(orderId).wineId(wineId).build();
        HttpEntity entity = new HttpEntity<>(wineOrder, headers);

        Orders actual = restTemplate.exchange(baseUrl + "/orders/wine/add", PUT, entity, Orders.class).getBody();

        assertEquals(orderArraySize.intValue(), actual.getWineIds().size());

        Wine wine = wineHelper.getWine(wineId, sessionId);

        assertEquals(wineStockToCompare, wine.getStock());
    }

    void removeWine(Integer orderId, Integer wineId, String sessionId, Integer orderArraySize,
                    Integer wineStockToCompare) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("sessionId", sessionId);
        OrderController.WineOrder wineOrder = OrderController.WineOrder.builder().orderId(orderId).wineId(wineId).build();
        HttpEntity entity = new HttpEntity<>(wineOrder, headers);

        Orders actual = restTemplate.exchange(baseUrl + "/orders/wine/remove", PUT, entity, Orders.class).getBody();

        assertEquals(orderArraySize.intValue(), actual.getWineIds().size());

        Wine wine = wineHelper.getWine(wineId, sessionId);

        assertEquals(wineStockToCompare, wine.getStock());
    }

    Integer createOrder(String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("sessionId", sessionId);
        HttpEntity entity = new HttpEntity<>(headers);
        Orders actual = restTemplate.exchange(baseUrl + "/orders", POST, entity, Orders.class).getBody();

        assertNotNull(actual);

        return actual.getId();
    }

    Orders getOrder(Integer orderId, String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("sessionId", sessionId);
        HttpEntity entity = new HttpEntity<>(headers);
        Orders actual = restTemplate.exchange(baseUrl + "/orders/" + orderId, GET, entity, Orders.class).getBody();

        assertNotNull(actual);

        return actual;
    }

    void finalizeOrder(Integer orderId, String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("sessionId", sessionId);
        HttpEntity entity = new HttpEntity<>(headers);

        restTemplate.exchange(baseUrl + "/orders" + "/" + orderId, DELETE, entity, Void.class);
    }
}

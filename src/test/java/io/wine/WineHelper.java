package io.wine;

import io.wine.model.Wine;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.HttpMethod.*;

class WineHelper {

    private RestTemplate restTemplate;
    private String baseUrl;

    WineHelper(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    void assertWines(Wine expected, Wine actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getStock(), actual.getStock());
    }

    Wine getWine(Integer wineId, String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("sessionId", sessionId);
        HttpEntity entity = new HttpEntity<>(headers);

        Wine actual = restTemplate.exchange(baseUrl + "/wines" + "/" + wineId, GET, entity, Wine.class).getBody();

        assertNotNull(actual);

        return actual;
    }

    int createWine(Wine wine, String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("sessionId", sessionId);
        HttpEntity entity = new HttpEntity<>(wine, headers);
        Wine actual = restTemplate.exchange(baseUrl + "/wines", POST, entity, Wine.class).getBody();

        assertWines(wine, actual);

        return actual.getId();
    }

    List<Wine> getAllWinesInOnePage(String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("sessionId", sessionId);
        HttpEntity entity = new HttpEntity<>(headers);
        List<Wine> body = restTemplate.exchange(baseUrl + "/wines?page=0&size=10", GET, entity,
                new ParameterizedTypeReference<WrapperGetAll>() {
                }).getBody().content;

        assertNotNull(body);

        return body;
    }

    static class WrapperGetAll {
        private List<Wine> content;

        public WrapperGetAll() {
        }

        public void setContent(List<Wine> content) {
            this.content = content;
        }
    }

    Wine updateWine(Wine wine, String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("sessionId", sessionId);
        HttpEntity entity = new HttpEntity<>(wine, headers);
        restTemplate.exchange(baseUrl + "/wines/" + wine.getId(), PUT, entity, Void.class);

        Wine actual = getWine(wine.getId(), sessionId);

        assertWines(wine, actual);

        return actual;
    }

    void deleteWine(Integer wineId, String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("sessionId", sessionId);
        HttpEntity entity = new HttpEntity<>(headers);
        restTemplate.exchange(baseUrl + "/wines/" + wineId, DELETE, entity, Void.class);

        try {
            getWine(wineId, sessionId);
        } catch (HttpClientErrorException e) {
            assertEquals(e.getRawStatusCode(), 404);
        }
    }
}

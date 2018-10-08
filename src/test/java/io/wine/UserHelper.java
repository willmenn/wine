package io.wine;

import io.wine.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

 class UserHelper {

    private RestTemplate restTemplate;
    private String baseUrl;

     UserHelper(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

     void createUser(User user) {
        HttpEntity entity = new HttpEntity<>(user);
        User actual = restTemplate.exchange(baseUrl + "/users", POST, entity, User.class).getBody();

        assertEquals(user.getUsername(), actual.getUsername());
        assertEquals(user.getPassword(), actual.getPassword());
    }

     void logout(String username, String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("sessionId", sessionId);
        HttpEntity entity = new HttpEntity<>(headers);

        restTemplate.exchange(baseUrl + "/users/" + username, DELETE, entity, Void.class);
    }

     String auth(String username, String password) {
        String sessionId = restTemplate
                .exchange(baseUrl + "/users?username=" + username + "&password=" + password,
                        GET, HttpEntity.EMPTY, String.class).getBody();

        assertNotNull(sessionId);

        return sessionId;
    }
}

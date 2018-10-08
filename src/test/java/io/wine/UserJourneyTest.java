package io.wine;

import io.wine.model.User;
import io.wine.model.Wine;
import io.wine.repository.OrdersRepository;
import io.wine.repository.UserRepository;
import io.wine.repository.WineRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

public class UserJourneyTest extends WineApplicationTests {

    private static final String USERNAME = "william";
    private static final String PASSWORD = "1234";

    @LocalServerPort
    private Integer port;

    private WineHelper wineHelper;

    private UserHelper userHelper;

    private OrderHelper orderHelper;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WineRepository wineRepository;

    @Before
    public void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://localhost:" + port;
        wineHelper = new WineHelper(restTemplate, baseUrl);
        userHelper = new UserHelper(restTemplate, baseUrl);
        orderHelper = new OrderHelper(restTemplate, baseUrl, wineHelper);
    }

    @After
    public void tearDown() {
        ordersRepository.deleteAll();
        userRepository.deleteAll();
        wineRepository.deleteAll();
    }

    @Test
    public void createOrderAndFinishOrderUserJourneyTest() {
        User user = User.builder().username(USERNAME).password(PASSWORD).build();
        userHelper.createUser(user);
        String sessionId = userHelper.auth(user.getUsername(), user.getPassword());
        Wine wine = Wine.builder().description("description Random").name("tinto").stock(1).build();

        int wineId = wineHelper.createWine(wine, sessionId);
        Integer orderId = orderHelper.createOrder(sessionId);

        orderHelper.addWine(orderId, wineId, sessionId, 1, 0);
        orderHelper.removeWine(orderId, wineId, sessionId, 0, 1);

        orderHelper.finalizeOrder(orderId, sessionId);

        userHelper.logout(user.getUsername(), sessionId);
    }

    @Test
    public void createWineStoreUserJourneyTest() {
        User user = User.builder().username(USERNAME).password(PASSWORD).build();
        userHelper.createUser(user);
        String sessionId = userHelper.auth(user.getUsername(), user.getPassword());
        Wine wine1 = Wine.builder().description("description Random1").name("tinto1").stock(1).build();
        Wine wine2 = Wine.builder().description("description Random2").name("tinto2").stock(2).build();
        Wine wine3 = Wine.builder().description("description Random3").name("tinto3").stock(3).build();

        int wineId1 = wineHelper.createWine(wine1, sessionId);
        int wineId2 = wineHelper.createWine(wine2, sessionId);
        int wineId3 = wineHelper.createWine(wine3, sessionId);

        Wine wine1Get = wineHelper.getWine(wineId1, sessionId);
        wineHelper.assertWines(wine1, wine1Get);
        Wine wine2Get = wineHelper.getWine(wineId2, sessionId);
        wineHelper.assertWines(wine2, wine2Get);
        Wine wine3Get = wineHelper.getWine(wineId3, sessionId);
        wineHelper.assertWines(wine3, wine3Get);

        wineHelper.deleteWine(wine3Get.getId(), sessionId);

        String whiteWineName = "Vinho Branco";
        Wine wineUpdated = wineHelper.updateWine(wine2Get.toBuilder().name(whiteWineName).build(), sessionId);
        assertEquals(whiteWineName, wineUpdated.getName());

        userHelper.logout(user.getUsername(), sessionId);
    }


}

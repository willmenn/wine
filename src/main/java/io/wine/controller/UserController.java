package io.wine.controller;

import io.wine.exception.UserAuthenticationFailedException;
import io.wine.model.User;
import io.wine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository repository;
    private ConcurrentHashMap<String, User> sessionMap;
    private Random random;

    @Autowired
    public UserController(UserRepository repository,
                          @Qualifier("sessionMap") ConcurrentHashMap<String, User> sessionMap) {
        this.repository = repository;
        this.sessionMap = sessionMap;
        this.random = new Random();
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public User createUser(@RequestBody User user) {
        return repository.save(user);
    }

    @RequestMapping(method = GET)
    public String authenticate(@RequestParam String username, @RequestParam String password) {
        User user = repository.findByUsernameAndAndPassword(username, password);
        if (user != null) {
            String sessionId = Long.toHexString(random.nextLong());
            if (!sessionMap.contains(sessionId)) {
                sessionMap.put(sessionId, user);
                return sessionId;
            }
        }

        throw new UserAuthenticationFailedException("Error Authenticating.");
    }

    @RequestMapping(value = "/{username}", method = DELETE)
    @ResponseStatus(NO_CONTENT)
    public void logout(@PathVariable("username") String username,
                       @RequestHeader("sessionId") String sessionId) {
        String sessionUsername = sessionMap.get(sessionId).getUsername();
        if (sessionUsername.equals(username)) {
            sessionMap.remove(sessionId);
        }
    }
}

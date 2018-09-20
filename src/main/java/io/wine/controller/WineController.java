package io.wine.controller;

import io.wine.exception.WineNotFoundException;
import io.wine.model.Wine;
import io.wine.repository.WineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/wines")
public class WineController {

    private WineRepository repository;

    @Autowired
    public WineController(WineRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    public Wine createWine(@RequestBody Wine wine) {
        return repository.save(wine);
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    @ResponseStatus(OK)
    public void deleteWine(@PathVariable("id") Integer id) {
        repository.deleteById(id);
    }

    @RequestMapping(value = "/{id}", method = GET)
    @ResponseStatus(OK)
    public Wine getWine(@PathVariable("id") Integer id) {
        return repository.findById(id)
                .orElseThrow(WineNotFoundException::new);
    }

    @RequestMapping(method = PUT)
    @ResponseStatus(OK)
    public Wine updateWine(@RequestBody Wine wine) {
        return repository.save(wine);
    }
}

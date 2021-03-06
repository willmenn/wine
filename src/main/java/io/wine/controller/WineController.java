package io.wine.controller;

import io.wine.exception.WineNotFoundException;
import io.wine.model.Wine;
import io.wine.repository.WineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/wines")
public class WineController {

    private WineRepository repository;

    @Autowired
    public WineController(WineRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Wine createWine(@RequestBody Wine wine) {
        return repository.save(wine);
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    @ResponseStatus(OK)
    public void deleteWine(@PathVariable("id") Integer id) {
        repository.deleteById(id);
    }

    @RequestMapping(value = "/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public WineResource getWine(@PathVariable("id") Integer id) {
        return new WineResource(repository.findById(id)
                .orElseThrow(WineNotFoundException::new));
    }

    @RequestMapping(value = "/{id}", method = PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public void updateWine(@PathVariable("id") Integer id, @RequestBody Wine wine) {
        Optional<Wine> wineTobeUpdated = repository.findById(id);
        wineTobeUpdated.ifPresent(w -> {
            Wine newWine = w.toBuilder()
                    .id(id)
                    .name(wine.getName() != null ? wine.getName() : w.getName())
                    .stock(wine.getStock() != null ? wine.getStock() : w.getStock())
                    .description(wine.getDescription() != null ? wine.getDescription() : w.getDescription())
                    .build();
            repository.save(newWine);
        });
    }

    @RequestMapping(method = GET)
    public Iterable<Wine> getAll(@RequestParam("page") Integer page,
                                 @RequestParam("size") Integer size) {
        PageRequest request = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        return repository.findAll(request);
    }
}

package io.wine.controller;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.wine.model.Wine;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class WineResource extends ResourceSupport {
    @JsonUnwrapped
    private final Wine exchangeRate;

    WineResource(Wine exchangeRate) {
        this.exchangeRate = exchangeRate;

        add(linkTo(methodOn(WineController.class).getWine(exchangeRate.getId())).withSelfRel());
    }
}

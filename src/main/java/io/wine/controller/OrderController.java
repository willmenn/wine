package io.wine.controller;

import io.wine.model.Orders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    public Orders createOrder(@RequestBody Orders wine) {
        //save wine
        return wine;
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    @ResponseStatus(OK)
    public Orders deleteOrder(@PathVariable("id") String id) {
        return new Orders();
    }

    @RequestMapping(value = "/{id}", method = GET)
    @ResponseStatus(OK)
    public Orders getOrder(@PathVariable("id") String id) {
        return new Orders();
    }

    @RequestMapping(method = PUT)
    @ResponseStatus(OK)
    public Orders updateOrder(@RequestBody Orders wine) {
        //update wine
        return wine;
    }
}

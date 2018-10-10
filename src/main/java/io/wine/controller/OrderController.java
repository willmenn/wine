package io.wine.controller;

import io.wine.exception.OrderNotFoundException;
import io.wine.model.Orders;
import io.wine.repository.OrdersRepository;
import io.wine.service.OrderService;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    private OrderService service;
    private OrdersRepository repository;

    @Autowired
    public OrderController(OrderService service, OrdersRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @RequestMapping(method = POST, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Orders createOrder() {
        return repository.save(new Orders());
    }

    @RequestMapping(value = "/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Orders getOrder(@PathVariable("id") Integer id) {
        return repository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    @RequestMapping(value = "/wine/add", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Orders addWineToOrder(@RequestBody WineOrder wineOrder) {
        return service.addWineToOrder(wineOrder.wineId, wineOrder.orderId);
    }

    @RequestMapping(value = "/wine/remove", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Orders removeWineToOrder(@RequestBody WineOrder wineOrder) {
        return service.removeWineToOrder(wineOrder.wineId, wineOrder.orderId);
    }

    @RequestMapping(method = DELETE, value = "/{orderId}")
    @ResponseStatus(NO_CONTENT)
    public void finalizeOrder(@PathVariable("orderId") Integer orderId) {
        repository.deleteById(orderId);
    }

    @Data
    @Builder
    public static class WineOrder {
        private Integer wineId;
        private Integer orderId;
    }
}

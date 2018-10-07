package io.wine.controller;

import io.wine.model.Orders;
import io.wine.service.OrderService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    private OrderService service;

    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Orders createOrder(@RequestBody Orders order) {
        return order;
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    @ResponseStatus(OK)
    public Orders deleteOrder(@PathVariable("id") String id) {
        return new Orders();
    }

    @RequestMapping(value = "/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Orders getOrder(@PathVariable("id") String id) {
        return new Orders();
    }

    @RequestMapping(value = "/wine/add",method = PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Orders addWineToOrder(@RequestBody WineOrder wineOrder) {
        return service.addWineToOrder(wineOrder.wineId, wineOrder.orderId);
    }

    @RequestMapping(value = "/wine/remove",method = PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Orders removeWineToOrder(@RequestBody WineOrder wineOrder) {
        return service.removeWineToOrder(wineOrder.wineId, wineOrder.orderId);
    }

    @Data
    class WineOrder {
        private Integer wineId;
        private Integer orderId;
    }
}

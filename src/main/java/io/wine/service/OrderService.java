package io.wine.service;

import io.wine.exception.OrderNotFoundException;
import io.wine.exception.WineNotFoundException;
import io.wine.model.Orders;
import io.wine.model.Wine;
import io.wine.repository.OrdersRepository;
import io.wine.repository.WineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    private OrdersRepository ordersRepository;
    private WineRepository wineRepository;

    @Autowired
    public OrderService(OrdersRepository ordersRepository, WineRepository wineRepository) {
        this.ordersRepository = ordersRepository;
        this.wineRepository = wineRepository;
    }

    public Orders addWineToOrder(Integer wineId, Integer orderId) {
        wineRepository.findById(wineId).orElseThrow(WineNotFoundException::new);
        Orders orders = ordersRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        orders.getWineIds().add(wineId);
        return ordersRepository.save(orders);
    }

    public Orders removeWineToOrder(Integer wineId, Integer orderId) {
        wineRepository.findById(wineId).orElseThrow(WineNotFoundException::new);
        Orders orders = ordersRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        orders.getWineIds().remove(wineId);
        return ordersRepository.save(orders);
    }
}

package io.wine.service;

import io.wine.exception.DoNotHaveStockException;
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
        Wine wine = wineRepository.findById(wineId).orElseThrow(WineNotFoundException::new);

        if (wine.getStock() > 0) {
            Orders orders = ordersRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
            orders.getWineIds().add(wineId);
            removeOneUnitFromStock(wine);
            return ordersRepository.save(orders);
        } else {
            throw new DoNotHaveStockException();
        }
    }

    public Orders removeWineToOrder(Integer wineId, Integer orderId) {
        Wine wine = wineRepository.findById(wineId).orElseThrow(WineNotFoundException::new);
        Orders orders = ordersRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        orders.getWineIds().remove(wineId);

        addOneUnitToStock(wine);

        return ordersRepository.save(orders);
    }

    private void addOneUnitToStock(Wine wine) {
        wine.setStock(wine.getStock() + 1);
        wineRepository.save(wine);
    }

    private void removeOneUnitFromStock(Wine wine) {
        wine.setStock(wine.getStock() - 1);
        wineRepository.save(wine);
    }
}

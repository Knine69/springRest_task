package com.jhuguet.sb_taskv1.app.repositories;

import com.jhuguet.sb_taskv1.app.exceptions.NoExistingOrders;
import com.jhuguet.sb_taskv1.app.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "SELECT * FROM ORDERS o ORDER BY o.cost DESC LIMIT 1", nativeQuery = true)
    Order getHighestCostOrder() throws NoExistingOrders;
}

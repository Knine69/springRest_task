package com.jhuguet.sb_taskv1.app.repositories;

import com.jhuguet.sb_taskv1.app.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}

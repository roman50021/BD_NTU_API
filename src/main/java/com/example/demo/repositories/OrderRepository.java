package com.example.demo.repositories;

import com.example.demo.models.Menu;
import com.example.demo.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderItem, Integer> {
}

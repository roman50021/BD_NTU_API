package com.example.demo.repositories;

import com.example.demo.models.Menu;
import com.example.demo.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    Optional<Menu> findById(Integer id);

    Optional<Menu> findByName(String name);

    Optional<Menu> findByNameAndRestaurantId(String name, Integer restaurantId);
}

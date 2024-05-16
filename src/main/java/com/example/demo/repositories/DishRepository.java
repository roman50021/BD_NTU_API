package com.example.demo.repositories;

import com.example.demo.models.Dish;
import com.example.demo.models.Menu;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    Optional<Dish> findById(Long id);
    List<Dish> findByMenu(Menu menu);
    List<Dish> findByMenuId(Long menuId);

    List<Dish> findAll(Specification<Dish> specification);
}
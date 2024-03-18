package com.example.demo.repositories;

import com.example.demo.models.Restaurant;
import com.example.demo.models.RestaurantAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantAddressRepository extends JpaRepository<RestaurantAddress, Integer> {
    void deleteByRestaurant(Restaurant restaurant);
}

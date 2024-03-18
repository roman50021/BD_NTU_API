package com.example.demo.repositories;

import com.example.demo.models.Courier;
import com.example.demo.models.User;
import com.example.demo.models.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {
    Optional<UserAddress> findByUser(User user);
}

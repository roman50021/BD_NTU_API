package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurant")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String description;
    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantAddress> addresses;
    @OneToMany(mappedBy = "restaurant")
    private List<Menu> menus;
    private String phone_number;
    private LocalDateTime open_time;
    private LocalDateTime close_time;
    private String website;

}

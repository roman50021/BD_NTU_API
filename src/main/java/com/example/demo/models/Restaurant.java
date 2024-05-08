package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurant", indexes = {
        @Index(name = "idx_name", columnList = "name"),
        @Index(name = "idx_open_time", columnList = "openTime"),
        @Index(name = "idx_close_time", columnList = "closeTime"),
        @Index(name = "idx_website", columnList = "website")
})
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String description;
    @OneToMany(mappedBy = "restaurant")
    @JsonIgnore
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<RestaurantAddress> addresses;
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    private List<Menu> menus;
    private String phoneNumber;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private String website;
}

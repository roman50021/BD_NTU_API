package com.example.demo.dto.restaurant.responses;

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
public class RestaurantWithAddressDto {
    private Integer id;
    private String name;
    private String description;
    private String phoneNumber;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private String website;
    private List<String> addresses;
    private String message;
}

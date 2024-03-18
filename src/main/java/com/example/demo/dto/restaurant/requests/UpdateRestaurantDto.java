package com.example.demo.dto.restaurant.requests;

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
public class UpdateRestaurantDto {
    private String name;
    private String newName;
    private String description;
    private String phoneNumber;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private String website;
    private List<RestaurantAddressDto> addresses;
}
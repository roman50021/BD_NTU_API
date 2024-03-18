package com.example.demo.dto.user.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserWithAddressDto {
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String city;
    private String street;
    private String houseNumber;
    private int apartmentNumber;
    private String message;
}

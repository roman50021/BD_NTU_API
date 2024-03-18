package com.example.demo.dto.user.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String city;
    private String street;
    private String houseNumber;
    private Integer apartmentNumber;
    private String message;
}

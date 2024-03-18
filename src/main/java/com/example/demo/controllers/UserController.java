package com.example.demo.controllers;

import com.example.demo.dto.MessageInfo;
import com.example.demo.dto.user.requests.CreateUserDto;
import com.example.demo.dto.user.requests.GetUserDto;
import com.example.demo.dto.user.requests.UpdateUserDto;
import com.example.demo.dto.user.requests.DeleteUserDto;
import com.example.demo.dto.user.responses.UserWithAddressDto;
import com.example.demo.models.User;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/create")
    public ResponseEntity<MessageInfo> createUser(@RequestBody CreateUserDto request){
        return service.createUser(request);
    }
    @GetMapping("/get")
    public ResponseEntity<UserWithAddressDto> getUser(@RequestBody GetUserDto request){
        return service.getUser(request);
    }
    @GetMapping("/all")
    public ResponseEntity<List<User>> getUsers() {
        return service.getUsers();
    }

    @PutMapping("/update")
    public ResponseEntity<MessageInfo> updateUser(@RequestBody UpdateUserDto request){
        return service.updateUser(request);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<MessageInfo> deleteUser(@RequestBody DeleteUserDto request){
        return service.deleteUser(request);
    }

}

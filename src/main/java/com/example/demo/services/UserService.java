package com.example.demo.services;

import com.example.demo.dto.MessageInfo;
import com.example.demo.dto.user.requests.CreateUserDto;
import com.example.demo.dto.user.requests.DeleteUserDto;
import com.example.demo.dto.user.requests.GetUserDto;
import com.example.demo.dto.user.requests.UpdateUserDto;
import com.example.demo.dto.user.responses.UserWithAddressDto;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.models.UserAddress;
import com.example.demo.repositories.UserAddressRepository;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService  {
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    public ResponseEntity<MessageInfo> createUser(CreateUserDto request) {
        try {
            // Проверяем существует ли пользователь с таким email
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent()) {
                // Если пользователь уже существует, возвращаем сообщение об ошибке
                return ResponseEntity.status(HttpStatus.CONFLICT).body(MessageInfo.builder().message("A user with this email already exists").build());
            }

            // Создаем новый адрес пользователя и сохраняем его в базе данных
            UserAddress newUserAddress = userAddressRepository.save(request.getAddress());

            // Создаем нового пользователя с сохраненным адресом
            User newUser = User.builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(request.getPassword())
                    .role(Role.USER)
                    .phoneNumber(request.getPhoneNumber())
                    .address(newUserAddress) // Устанавливаем сохраненный адрес
                    .dateOfRegistration(LocalDateTime.now())
                    .build();

            // Устанавливаем ссылку на пользователя в адресе
            newUserAddress.setUser(newUser);

            // Сохраняем пользователя и адрес в базе данных
            userRepository.save(newUser);

            // Формируем сообщение об успешном создании пользователя
            MessageInfo messageInfo = MessageInfo.builder()
                    .message("User successfully created")
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(messageInfo);

        } catch (Exception e) {
            // Обработка исключений
            String errorMessage = "Error when creating user: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MessageInfo.builder().message(errorMessage).build());
        }
    }

    public ResponseEntity<UserWithAddressDto> getUser(GetUserDto request) {
        try {
            // Ищем пользователя по email
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Ищем адрес пользователя
            UserAddress userAddress = userAddressRepository.findByUser(user)
                    .orElseThrow(() -> new IllegalArgumentException("User address not found"));

            // Создаем объект UserWithAddressDto
            UserWithAddressDto userWithAddressDto = UserWithAddressDto.builder()
                    .message(null)
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .city(userAddress.getCity())
                    .street(userAddress.getStreet())
                    .houseNumber(userAddress.getHouseNumber())
                    .apartmentNumber(userAddress.getApartmentNumber())
                    .build();

            // Возвращаем объект пользователя с адресом
            return ResponseEntity.ok(userWithAddressDto);

        } catch (IllegalArgumentException e) {
            // Обработка случая, когда пользователь или его адрес не найдены
            String errorMessage = "Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserWithAddressDto.builder().message(errorMessage).build());
        } catch (Exception e) {
            // Обработка других исключений
            String errorMessage = "Error when getting user: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserWithAddressDto.builder().message(errorMessage).build());
        }
    }
    @Cacheable("usersCache")
    public ResponseEntity<List<User>> getUsers() {
        try {

            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            String errorMessage = "Error when getting users: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<MessageInfo> updateUser(UpdateUserDto request) {
        try {
            // Получаем пользователя по его id
            Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // Обновляем поля пользователя
                if (request.getFirstname() != null) {
                    user.setFirstname(request.getFirstname());
                }
                if (request.getLastname() != null) {
                    user.setLastname(request.getLastname());
                }
                if (request.getEmail() != null) {
                    user.setEmail(request.getEmail());
                }
                if (request.getPhoneNumber() != null) {
                    user.setPhoneNumber(request.getPhoneNumber());
                }

                // Сохраняем обновленного пользователя
                userRepository.save(user);

                // Получаем адрес пользователя
                UserAddress userAddress = user.getAddress();
                if (userAddress != null) {
                    // Обновляем поля адреса, если они предоставлены
                    if (request.getCity() != null) {
                        userAddress.setCity(request.getCity());
                    }
                    if (request.getStreet() != null) {
                        userAddress.setStreet(request.getStreet());
                    }
                    if (request.getHouseNumber() != null) {
                        userAddress.setHouseNumber(request.getHouseNumber());
                    }
                    if (request.getApartmentNumber() != null) {
                        userAddress.setApartmentNumber(request.getApartmentNumber());
                    }

                    // Сохраняем обновленный адрес пользователя
                    userAddressRepository.save(userAddress);
                }

                // Формируем сообщение об успешном обновлении
                MessageInfo messageInfo = MessageInfo.builder()
                        .message("User updated successfully")
                        .build();

                return ResponseEntity.ok(messageInfo);
            } else {
                // Если пользователь не найден
                String errorMessage = "User not found";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageInfo.builder().message(errorMessage).build());
            }
        } catch (Exception e) {
            // Обработка исключений
            String errorMessage = "Error when updating user: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MessageInfo.builder().message(errorMessage).build());
        }
    }

    public ResponseEntity<MessageInfo> deleteUser(DeleteUserDto request) {
        try {
            // Находим пользователя по его email
            Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // Получаем адрес пользователя
                UserAddress userAddress = user.getAddress();

                // Удаляем адрес пользователя, если он существует
                if (userAddress != null) {
                    userAddressRepository.delete(userAddress);
                }

                // Удаляем пользователя
                userRepository.delete(user);

                // Формируем сообщение об успешном удалении
                MessageInfo messageInfo = MessageInfo.builder()
                        .message("User deleted successfully")
                        .build();

                return ResponseEntity.ok(messageInfo);
            } else {
                // Если пользователь не найден
                String errorMessage = "User not found";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageInfo.builder().message(errorMessage).build());
            }
        } catch (Exception e) {
            // Обработка исключений
            String errorMessage = "Error when deleting user: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MessageInfo.builder().message(errorMessage).build());
        }
    }
}

package com.example.demo.controllers;

import com.example.demo.dto.tasks.DishDTO;
import com.example.demo.dto.tasks.UserDTO;
import com.example.demo.models.User;
import com.example.demo.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

//    Поиск пользователей по email:
//    GET /api/task?email=roman1@example.com
    @GetMapping("/search")
    public ResponseEntity<?> findUserByEmail(@RequestParam String email) {
        UserDTO userDTO = taskService.findUserByEmail(email);
        if (userDTO != null) {
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    Поиск пользователей по нескольким параметрам (например, имя и фамилия):
//    GET /api/users?firstname=John&lastname=Doe
    @GetMapping("/users")
    public ResponseEntity<?> findUsersByFirstnameAndLastname(@RequestParam String firstname, @RequestParam String lastname) {
    List<UserDTO> users = taskService.findUsersByFirstnameAndLastname(firstname, lastname);
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    Поиск по параметрам, связанным с другими записями
//    GETapi/task/menu/{menuID}/dishes
    @GetMapping("/menu/{menuId}")
    public ResponseEntity<?> findDishesByMenuId(@PathVariable Long menuId) {
        List<DishDTO> dishDTOs = taskService.findDishesByMenuId(menuId);
        return ResponseEntity.ok(dishDTOs);
    }
}

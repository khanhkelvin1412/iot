package com.hust.khanhkelvin.web.rest;

import com.hust.khanhkelvin.dto.User;
import com.hust.khanhkelvin.dto.request.UserRegisterRequest;
import com.hust.khanhkelvin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerAccount(@RequestBody @Valid UserRegisterRequest request) {
        User user = userService.register(request);
        return ResponseEntity.ok(user);
    }
}

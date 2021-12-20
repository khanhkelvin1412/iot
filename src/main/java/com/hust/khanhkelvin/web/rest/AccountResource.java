package com.hust.khanhkelvin.web.rest;

import com.hust.khanhkelvin.dto.User;
import com.hust.khanhkelvin.dto.request.UserLoginRequest;
import com.hust.khanhkelvin.dto.request.UserRegisterRequest;
import com.hust.khanhkelvin.dto.response.AuthToken;
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
public class AccountResource {

    private final UserService userService;

    public AccountResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthToken> authenticate(@RequestBody @Valid UserLoginRequest request) {
        AuthToken authToken = userService.authenticate(request);
        return ResponseEntity.ok(authToken);
    }

    @PostMapping("/account/activate")
    public ResponseEntity<AuthToken> active(@RequestBody @Valid UserLoginRequest request) {
        AuthToken authToken = userService.authenticate(request);
        return ResponseEntity.ok(authToken);
    }
}

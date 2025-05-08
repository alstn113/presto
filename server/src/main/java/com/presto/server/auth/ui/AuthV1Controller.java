package com.presto.server.auth.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthV1Controller {

    @PostMapping("/login")
    public void login() {
    }

    @PostMapping("/register")
    public void register() {
    }

    @PostMapping("/logout")
    public void logout() {
    }
}

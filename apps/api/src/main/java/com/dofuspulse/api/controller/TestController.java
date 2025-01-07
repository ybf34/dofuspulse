package com.dofuspulse.api.controller;

import com.dofuspulse.api.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {


    @GetMapping("/")
    public String test() {
        return "Dofus Pulse Api is running";
    }

    @GetMapping("/admin")
    public String test1(@AuthenticationPrincipal UserPrincipal principal) {

        return "Hello to the admin page " + principal.getEmail() + principal.getId() + principal.getRole().getAuthorities();

    }
}

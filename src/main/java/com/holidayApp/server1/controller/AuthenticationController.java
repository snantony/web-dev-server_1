package com.holidayApp.server1.controller;

import com.holidayApp.server1.model.AuthenticationResponse;
import com.holidayApp.server1.model.User;
import com.holidayApp.server1.service.AuthenticationService;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/user/register")
    public ResponseEntity<AuthenticationResponse> registerUser(
            @RequestBody User request
    ) {
        if(!authService.isUserDataValid(request)){
            return ResponseEntity.badRequest().body(new AuthenticationResponse(null, "Invalid user input."));
        }

        AuthenticationResponse response = authService.registerUser(request);

        if(!StringUtils.isNotBlank(response.getToken())){
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/vendor/register")
    public ResponseEntity<AuthenticationResponse> registerVendor(
            @RequestBody User request
    ) {
        if(!authService.isVendorDataValid(request)){
            return ResponseEntity.badRequest().body(new AuthenticationResponse(null, "Invalid user input."));
        }

        AuthenticationResponse response = authService.registerVendor(request);

        if(!StringUtils.isNotBlank(response.getToken())){
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> vendorLogin(
            @RequestBody User request
    ) {
        AuthenticationResponse response = authService.authenticate(request);

        if(!StringUtils.isNotBlank(response.getToken())){
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }
}

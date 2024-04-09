package com.holidayApp.server1.service;

import com.holidayApp.server1.model.AuthenticationResponse;
import com.holidayApp.server1.model.Role;
import com.holidayApp.server1.model.Token;
import com.holidayApp.server1.model.User;
import com.holidayApp.server1.repository.TokenRepository;
import com.holidayApp.server1.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;


    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, TokenRepository tokenRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
    }
    private void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }

    public boolean isVendorDataValid(User request){
        return StringUtils.isNotBlank(request.getEmail()) && StringUtils.isNotBlank(request.getCompanyName()) && StringUtils.isNotBlank(request.getPhone()) && StringUtils.isNotBlank(request.getPassword()) && StringUtils.isNotBlank(request.getAddress()) && StringUtils.isNotBlank(request.getConfirmPassword());
    }

    public boolean isUserDataValid(User request){
        return StringUtils.isNotBlank(request.getEmail()) && StringUtils.isNotBlank(request.getFirstName()) && StringUtils.isNotBlank(request.getPhone()) && StringUtils.isNotBlank(request.getPassword()) && StringUtils.isNotBlank(request.getLastName()) && StringUtils.isNotBlank(request.getConfirmPassword());
    }

    public boolean userExistsByEmail(String email){
        return repository.findByEmail(email).isPresent();
    }

    public boolean userExistsByPhone(String phone){
        return repository.findByPhone(phone).isPresent();
    }

    public boolean userExists(User request){
        return userExistsByEmail(request.getEmail()) || userExistsByPhone(request.getPhone());
    }

    public AuthenticationResponse registerUser(User request) {

        // to check if user already exist
        if(userExists(request)) {
            return new AuthenticationResponse(null, "User already exist");
        }

        if(!Objects.equals(request.getPassword(), request.getConfirmPassword())){
            return new AuthenticationResponse(null, "Password and confirm password does not match.");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCompanyName(null);
        user.setAddress(null);
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        user.setRole(Role.USER);

        user = repository.save(user);

        String jwt = jwtService.generateToken(user);

        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt, "User registration was successful");

    }

    public AuthenticationResponse registerVendor(User request) {

        // to check if user already exist
        if(userExists(request)) {
            return new AuthenticationResponse(null, "User already exist");
        }

        if(!Objects.equals(request.getPassword(), request.getConfirmPassword())){
            return new AuthenticationResponse(null, "Password and confirm password does not match.");
        }

        User user = new User();
        user.setFirstName(null);
        user.setLastName(null);
        user.setCompanyName(request.getCompanyName());
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        user.setRole(Role.VENDOR);

        user = repository.save(user);

        String jwt = jwtService.generateToken(user);

        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt, "Vendor registration was successful");

    }

    public AuthenticationResponse authenticate(User request) {

        if(!StringUtils.isNotBlank(request.getEmail()) || !StringUtils.isNotBlank(request.getPassword())){
            return new AuthenticationResponse(null, "Invalid credentials");
        }

        try {
            User user = repository.findByEmail(request.getEmail()).orElseThrow();

            if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
                return new AuthenticationResponse(null, "Invalid credentials");
            }

            String jwt = jwtService.generateToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(jwt, user);

            return new AuthenticationResponse(jwt, "User login was successful");
        } catch (NoSuchElementException e) {
            return new AuthenticationResponse(null, "Invalid credentials");
        }
    }
}

package com.holidayApp.server1.repository;

import com.holidayApp.server1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    Optional<User> findById(int id);
}

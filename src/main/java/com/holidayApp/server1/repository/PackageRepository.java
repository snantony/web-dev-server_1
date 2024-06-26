package com.holidayApp.server1.repository;

import com.holidayApp.server1.model.Package;
import com.holidayApp.server1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PackageRepository extends JpaRepository<Package, Integer> {
    @Query("select p from Package p where p.user.id = (select u.id from User u where u.email = :email)")
    List<Package> findAllByVendorEmail(String email);

    Optional<Package> findById(int id);
}

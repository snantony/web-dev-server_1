package com.holidayApp.server1.repository;

import com.holidayApp.server1.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PackageRepository extends JpaRepository<Package, Integer> {
    @Query("select p from Package p where p.user.id = (select u.id from User u where u.email = :email)")
    List<Package> findAllByVendorEmail(String email);


}

package com.holidayApp.server1.repository;

import com.holidayApp.server1.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<Package, Integer> {
}

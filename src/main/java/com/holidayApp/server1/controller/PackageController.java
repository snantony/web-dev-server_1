package com.holidayApp.server1.controller;

import com.holidayApp.server1.model.Package;
import com.holidayApp.server1.service.PackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor/package")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addPackage(@RequestHeader(name = "Authorization") String authorizationHeader, @RequestBody Package request){

        if(!packageService.addPackage(authorizationHeader, request)){
            return ResponseEntity.badRequest().body("Invalid data.");
        }

        return ResponseEntity.ok("Package added successfully.");
    }
}

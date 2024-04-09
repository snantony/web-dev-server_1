package com.holidayApp.server1.controller;

import com.holidayApp.server1.model.Package;
import com.holidayApp.server1.service.PackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/package")
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

    @GetMapping("/get")
    public ResponseEntity<List<Package>> getPackages(){
        return ResponseEntity.ok(packageService.findAllPackages());
    }

    @GetMapping("/get-package/{packageId}")
    public ResponseEntity<Package> getPackageDetails(@PathVariable("packageId") String packageId) {
        byte[] decodedBytes = Base64.getDecoder().decode(packageId);
        String decodedId = new String(decodedBytes);
        return ResponseEntity.ok(packageService.getPackageDetail(decodedId));
    }
}

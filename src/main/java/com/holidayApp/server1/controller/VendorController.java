package com.holidayApp.server1.controller;

import com.holidayApp.server1.model.User;
import com.holidayApp.server1.service.VendorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping("/get/{vendorId}")
    public ResponseEntity<User> getVendor(@PathVariable("vendorId") String vendorId){
        byte[] decodedBytes = Base64.getDecoder().decode(vendorId);
        String decodedId = new String(decodedBytes);

        return ResponseEntity.ok(vendorService.getVendorDetail(decodedId));
    }
}

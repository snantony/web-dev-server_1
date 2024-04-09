package com.holidayApp.server1.service;

import com.holidayApp.server1.model.Package;
import com.holidayApp.server1.model.User;
import com.holidayApp.server1.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class VendorService {

    private final UserRepository userRepository;

    public VendorService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getVendorDetail(String vendorId){
        return  userRepository.findById(Integer.parseInt(vendorId)).orElseThrow(null);
    }
}

package com.holidayApp.server1.service;

import com.holidayApp.server1.model.Package;
import com.holidayApp.server1.model.User;
import com.holidayApp.server1.repository.PackageRepository;
import com.holidayApp.server1.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    private final PackageRepository packageRepository;

    public PackageService(JwtService jwtService, UserRepository userRepository, AuthenticationService authenticationService, PackageRepository packageRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.packageRepository = packageRepository;
    }

    public boolean isDataValid(Package request){
        return StringUtils.isNotBlank(request.getPackageName()) && request.getPrice() != null && StringUtils.isNotBlank(request.getDescription());
    }

    public boolean addPackage(String authorizationHeader, Package request){
        String token = jwtService.getToken(authorizationHeader);

        String email = jwtService.extractEmail(token);

        if(!isDataValid(request)){
            return false;
        }

        if(!authenticationService.userExistsByEmail(email)){
            return false;
        }

        User user = userRepository.findByEmail(email).orElseThrow();

        Package pack = new Package();
        pack.setPackageName(request.getPackageName());
        pack.setPrice(request.getPrice());
        pack.setDescription(request.getDescription());
        pack.setUser(user);

        packageRepository.save(pack);

        return true;
    }

    public List<Package> getPackagesByVendor(String authorizationHeader){
        String token = jwtService.getToken(authorizationHeader);

        String email = jwtService.extractEmail(token);

        return  packageRepository.findAllByVendorEmail(email);
    }

    public List<Package> findAllPackages() {
        List<Package> packages = packageRepository.findAll();

        for (Package pack : packages) {
            pack.setVendorId(pack.getUser().getId());
            pack.setUser(null);
        }

        return packages;
    }
}

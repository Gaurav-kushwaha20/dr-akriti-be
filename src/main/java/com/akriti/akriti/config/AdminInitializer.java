package com.akriti.akriti.config;

import com.akriti.akriti.modules.user.entity.UserEntity;
import com.akriti.akriti.modules.user.enums.UserRole;
import com.akriti.akriti.modules.user.enums.UserType;
import com.akriti.akriti.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserService userService;

    @Value("${application.security.admin.email}")
    private String email;

    @Value("${application.security.admin.password}")
    private String password;


    @Override
    public void run(String... args) throws Exception {
        if (!userService.doesAnySuperAdminExist()) {
            System.out.println("User doesnt exist");
            UserEntity user = UserEntity.builder()
                    .firstName("Admin")
                    .lastName("Admin")
                    .username("admin")
                    .email(email)
                    .phone("9800000001")
                    .role(UserRole.SUPER_ADMIN)
                    .userType(UserType.ADMIN)
                    .password(userService.encryptPassword(password))
                    .build();
            if (userService.saveUser(user) != null) System.out.println("Admin Created");
        }
    }
}

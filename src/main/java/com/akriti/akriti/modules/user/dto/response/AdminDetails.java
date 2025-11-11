package com.akriti.akriti.modules.user.dto.response;

import com.akriti.akriti.modules.user.enums.UserGender;
import com.akriti.akriti.modules.user.enums.UserRole;
import com.akriti.akriti.modules.user.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class AdminDetails {
    private String id;
    private String email;
    private String username;
    private String phone;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private UserGender gender;
    private UserRole role;
    private UserType userType;
}

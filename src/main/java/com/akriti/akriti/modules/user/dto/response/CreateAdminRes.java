package com.akriti.akriti.modules.user.dto.response;

import com.akriti.akriti.modules.user.enums.UserGender;
import com.akriti.akriti.modules.user.enums.UserRole;
import com.akriti.akriti.modules.user.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CreateAdminRes {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone_no;
    private String username;
    private String dob;
    private UserGender gender;
    private UserRole role;
    private UserType userType;
}

package com.akriti.akriti.modules.user.dto.response;

import com.akriti.akriti.modules.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AdminRes {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    private UserRole role;
}

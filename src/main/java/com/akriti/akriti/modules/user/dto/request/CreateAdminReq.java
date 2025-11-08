package com.akriti.akriti.modules.user.dto.request;

import lombok.Data;

@Data
public class CreateAdminReq {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String username;
    private String password;
    private String dob;
    private String gender;
}

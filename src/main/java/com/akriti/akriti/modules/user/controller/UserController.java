package com.akriti.akriti.modules.user.controller;

import com.akriti.akriti.dto.ApiResponse;
import com.akriti.akriti.modules.user.dto.request.CreateAdminReq;
import com.akriti.akriti.modules.user.dto.response.CreateAdminRes;
import com.akriti.akriti.modules.user.dto.response.Token;
import com.akriti.akriti.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/user", produces = "application/json")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/admin/create")
    public ResponseEntity<?> createAdmin(@RequestBody CreateAdminReq request) {
        Object userExist = userService.doesUserExist(request.getEmail(), request.getUsername(), request.getPhone());
        if(userExist instanceof String) return ResponseEntity.badRequest().body(new ApiResponse<>("User already exist", 400, false, userExist, null));
        CreateAdminRes createAdminRes = userService.createAdmin(request);
        Token token = userService.getNewToken(createAdminRes.getId(), createAdminRes.getRole().toString(), createAdminRes.getUserType().toString(), createAdminRes.getId(),createAdminRes.getFirstName(), createAdminRes.getLastName());
        return ResponseEntity.ok(new ApiResponse<>("Admin Created Successfully", 201, true, Map.of("user",createAdminRes,"token",token), null));
    }

    @GetMapping(value = "/admin/get-all")
    public ResponseEntity<?> getALlAdmin() {

        return ResponseEntity.ok("get all admin list");
    }
}

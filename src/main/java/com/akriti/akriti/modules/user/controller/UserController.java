package com.akriti.akriti.modules.user.controller;

import com.akriti.akriti.dto.ApiResponse;
import com.akriti.akriti.dto.PaginatedResponse;
import com.akriti.akriti.modules.user.dto.request.AdminLogin;
import com.akriti.akriti.modules.user.dto.request.AdminReq;
import com.akriti.akriti.modules.user.dto.response.AdminDetails;
import com.akriti.akriti.modules.user.dto.response.AdminRes;
import com.akriti.akriti.modules.user.dto.response.CreateAdminRes;
import com.akriti.akriti.modules.user.dto.response.Token;
import com.akriti.akriti.modules.user.entity.UserEntity;
import com.akriti.akriti.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/user", produces = "application/json")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/admin/create")
    public ResponseEntity<?> createAdmin(@RequestBody AdminReq request) {
        userService.doesUserExist(request.getEmail(), request.getUsername(), request.getPhone());
        CreateAdminRes createAdminRes = userService.createAdmin(request);
        Token token = userService.getNewToken(createAdminRes.getId(), createAdminRes.getRole().toString(), createAdminRes.getUserType().toString(), createAdminRes.getId(), createAdminRes.getFirstName(), createAdminRes.getLastName());
        return ResponseEntity.ok(new ApiResponse<>("Admin Created Successfully", 201, true, Map.of("user", createAdminRes, "token", token), null));
    }

    @GetMapping(value = "/admin/get-all")
    public ResponseEntity<?> getAllAdmin(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "") String search
    ) {
        Page<AdminRes> pageListAdmin = userService.getAllAdmin(page, pageSize, search);
        PaginatedResponse<AdminRes> paginatedResponse = userService.getPaginatedData(pageListAdmin);
        return ResponseEntity.ok(new ApiResponse<>("Admin fetched successfully", 200, true, paginatedResponse, null));
    }

    @PostMapping(value = "/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody AdminLogin request) {
        Map<String, Object> res = userService.adminLogin(request);
        return ResponseEntity.ok(new ApiResponse<>("Admin logged in", 200, true, res, null));
    }

    @GetMapping(value = "/admin/details/{id}")
    public ResponseEntity<?> getAdminDetails(@PathVariable String id) {
        AdminDetails res = userService.getAdminDetails(id);
        return ResponseEntity.ok().body(new ApiResponse<>("Admin details retrieved", 200, true, res, null));
    }

    @PutMapping(value = "/admin/update/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable String id, @RequestBody AdminReq request){
        UserEntity user = userService.getUserById(id);
        userService.doesUserExistAndIdNot(request.getEmail(), request.getUsername(), request.getPhone(), user.getId());
        UserEntity updatedUser = userService.updateAdmin( request,user);
        AdminDetails adminUpdated = userService.mapToAdminDetails(updatedUser);
        return ResponseEntity.ok(new ApiResponse<>("Admin updated successfully", 200, true, adminUpdated, null));
    }

}

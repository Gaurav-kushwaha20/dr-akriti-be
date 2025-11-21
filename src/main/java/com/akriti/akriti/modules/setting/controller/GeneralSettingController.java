package com.akriti.akriti.modules.setting.controller;

import com.akriti.akriti.dto.ApiResponse;
import com.akriti.akriti.modules.setting.dto.request.GeneralSettingReq;
import com.akriti.akriti.modules.setting.dto.response.GeneralSettingRes;
import com.akriti.akriti.modules.setting.service.GeneralSettingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/setting", produces = "application/json")
@RequiredArgsConstructor
public class GeneralSettingController {
    private final GeneralSettingService generalSettingService;

    @PostMapping(value = "/general-setting")
    public ResponseEntity<?>createGeneralSetting(@Valid @RequestBody GeneralSettingReq request){
        return ResponseEntity.ok(new ApiResponse<>("General setting created successfully",201,true,"General Setting data",null));
    }

    @GetMapping(value = "/general-setting")
    public ResponseEntity<?> getGeneralSetting(){
        GeneralSettingRes response = generalSettingService.getGeneralSetting();
        return ResponseEntity.ok(new ApiResponse<>("General setting retrieved",200,true,response, null));
    }

    @PutMapping(value = "/general-setting")
    public ResponseEntity<?> updateGeneralSetting(){
        return ResponseEntity.ok(new ApiResponse<>("General setting updated successfully", 200, true, "Updated data", null));
    }

    @DeleteMapping(value = "/general-setting")
    public ResponseEntity<?> deleteGeneralSetting(){
        return ResponseEntity.ok(new ApiResponse<>("General setting deleted", 200,true, "General setting deleted", null));
    }



}

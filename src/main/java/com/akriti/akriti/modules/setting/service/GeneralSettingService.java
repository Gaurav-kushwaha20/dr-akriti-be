package com.akriti.akriti.modules.setting.service;

import com.akriti.akriti.modules.setting.repo.GeneralSettingRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneralSettingService {
    private final GeneralSettingRepo generalSettingRepo;
}

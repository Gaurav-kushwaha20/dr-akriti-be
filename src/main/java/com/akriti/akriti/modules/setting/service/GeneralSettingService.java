package com.akriti.akriti.modules.setting.service;

import com.akriti.akriti.modules.setting.dto.response.GeneralSettingRes;
import com.akriti.akriti.modules.setting.entity.GeneralSettingEntity;
import com.akriti.akriti.modules.setting.repo.GeneralSettingRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneralSettingService {
    private final GeneralSettingRepo generalSettingRepo;

    public GeneralSettingRes getGeneralSetting() {
        GeneralSettingEntity generalSetting = generalSettingRepo.findFirst();
        return this.toGeneralSettingRes(generalSetting);
    }

    private GeneralSettingRes toGeneralSettingRes(GeneralSettingEntity generalSettingEntity) {
        if (generalSettingEntity == null)
            return null;
        return GeneralSettingRes.builder()
                .id(generalSettingEntity.getId())
                .favicon(generalSettingEntity.getFavicon())
                .logo(generalSettingEntity.getLogo())
                .date_format(generalSettingEntity.getDate_format())
                .language(generalSettingEntity.getLanguage())
                .timeZone(generalSettingEntity.getTimeZone())
                .createdAt(generalSettingEntity.getCreatedAt())
                .updatedAt(generalSettingEntity.getUpdatedAt())
                .build();
    }
}

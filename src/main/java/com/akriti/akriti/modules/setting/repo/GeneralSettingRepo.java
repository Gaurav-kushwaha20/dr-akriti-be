package com.akriti.akriti.modules.setting.repo;

import com.akriti.akriti.modules.setting.entity.GeneralSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneralSettingRepo extends JpaRepository<GeneralSettingEntity, String> {
}

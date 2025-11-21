package com.akriti.akriti.modules.setting.repo;

import com.akriti.akriti.modules.setting.entity.GeneralSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GeneralSettingRepo extends JpaRepository<GeneralSettingEntity, String> {

     @Query("SELECT gse FROM GeneralSettingEntity gse")
    GeneralSettingEntity findFirst();
}

package com.akriti.akriti.modules.setting.entity;

import com.akriti.akriti.modules.setting.enums.DateFormat;
import com.akriti.akriti.modules.setting.enums.Language;
import com.akriti.akriti.modules.setting.enums.TimeZone;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="general_setting")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralSettingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String favicon;
    private String logo;
    private DateFormat date_format;
    private Language language;
    private TimeZone timeZone;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

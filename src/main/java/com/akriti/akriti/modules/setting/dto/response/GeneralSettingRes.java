package com.akriti.akriti.modules.setting.dto.response;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.akriti.akriti.modules.setting.enums.DateFormat;
import com.akriti.akriti.modules.setting.enums.Language;
import com.akriti.akriti.modules.setting.enums.TimeZone;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralSettingRes {
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

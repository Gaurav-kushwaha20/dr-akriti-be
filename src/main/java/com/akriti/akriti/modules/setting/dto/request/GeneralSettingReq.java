package com.akriti.akriti.modules.setting.dto.request;

import com.akriti.akriti.modules.setting.enums.DateFormat;
import com.akriti.akriti.modules.setting.enums.Language;
import com.akriti.akriti.modules.setting.enums.TimeZone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralSettingReq {


    private String id;

    @NotNull
    private String favicon;

    @NotNull
    private String logo;

    @NotNull
    @NotBlank
    private DateFormat date_format;

    @NotNull
    @NotBlank
    private Language language;

    @NotNull
    @NotBlank
    private TimeZone timeZone;
}

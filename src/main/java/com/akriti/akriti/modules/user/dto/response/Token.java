package com.akriti.akriti.modules.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class Token {
    private String access;
    private String refresh;
}

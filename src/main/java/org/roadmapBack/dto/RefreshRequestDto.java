package org.roadmapBack.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshRequestDto {
    
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}
package com.example.asset_monitoring.dto;

public record AssetRequest(
    
    @NotBlank(message = "Code is required!")
    @Size(max = 32 , message = "Code cannot exceed 32 characters!")
    @Pattern(
        regexp = "^[A-Z0-9-]+$",
        message = "Code must contain uppercase, digits and hyphens"
    )
    String code, 

    @NotBlank(message = "Name is required")
    @Size(max = 32)
    String name
){};
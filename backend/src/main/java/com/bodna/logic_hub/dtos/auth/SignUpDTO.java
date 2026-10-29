package com.bodna.logic_hub.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpDTO(
    @NotBlank @Email
    @Size(min = 6, max = 254)
    String email,

    @NotBlank @Size(min = 3, max = 20)
    String username,

    @NotBlank
    String password
) {
}

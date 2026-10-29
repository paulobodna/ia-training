package com.bodna.logic_hub.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
    @NotBlank @Email
    @Size(min = 6, max = 254)
    String login,

    @NotBlank
    String password
) {
}

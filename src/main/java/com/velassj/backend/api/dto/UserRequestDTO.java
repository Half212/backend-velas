package com.velassj.backend.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotBlank(message = "O e-mail é obrigatório")
    private String email;

    @NotBlank(message = "O telefone/celular é obrigatório")
    private String phone;

    @NotBlank(message = "A senha é obrigatória")
    private String password;

    @NotBlank(message = "A confirmação de senha é obrigatória")
    private String confirmPassword;

    @NotNull(message = "O ID do cargo (role) é obrigatório")
    private Long roleId;
}

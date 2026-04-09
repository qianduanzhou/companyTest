package com.company.api.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * User create/update DTO.
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "username must not be blank")
    @Size(max = 64, message = "username length must be <= 64")
    private String username;

    @Email(message = "email format is invalid")
    @Size(max = 128, message = "email length must be <= 128")
    private String email;

    @Pattern(regexp = "^1\\d{10}$", message = "phone format is invalid")
    private String phone;
}

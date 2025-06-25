package com.biblioteca.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuncionarioDTO {
    private Long id;
    
    @NotBlank(message = "O nome do funcionário é obrigatório")
    @Size(min = 2, max = 150, message = "O nome deve ter entre 2 e 150 caracteres")
    private String nome;
    
    @Size(min = 2, max = 50, message = "O cargo deve ter entre 2 e 50 caracteres")
    private String cargo;
    
    @Pattern(regexp = "^\\(\\d{2}\\)\\s\\d{4,5}\\-\\d{4}$", message = "Telefone inválido. Use o formato: (XX) XXXXX-XXXX ou (XX) XXXX-XXXX")
    private String telefone;
    
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 100, message = "O email deve ter no máximo 100 caracteres")
    private String email;
    
    @NotBlank(message = "O nome de usuário é obrigatório")
    @Size(min = 4, max = 50, message = "O nome de usuário deve ter entre 4 e 50 caracteres")
    private String username;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String password;
}
package com.biblioteca.biblioteca.dto;

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
public class UsuarioDTO {
    private Long id;
    
    @NotBlank(message = "O nome do usuário é obrigatório")
    @Size(min = 2, max = 150, message = "O nome deve ter entre 2 e 150 caracteres")
    private String nome;
    
    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$", message = "CPF inválido. Use o formato: XXX.XXX.XXX-XX")
    private String cpf;
    
    @Email(message = "Email inválido")
    @Size(max = 100, message = "O email deve ter no máximo 100 caracteres")
    private String email;
    
    @Pattern(regexp = "^\\(\\d{2}\\)\\s\\d{4,5}\\-\\d{4}$", message = "Telefone inválido. Use o formato: (XX) XXXXX-XXXX ou (XX) XXXX-XXXX")
    private String telefone;
}
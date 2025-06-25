package com.biblioteca.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivroDTO {
    private Long id;
    
    @NotBlank(message = "O título do livro é obrigatório")
    @Size(min = 2, max = 150, message = "O título deve ter entre 2 e 150 caracteres")
    private String titulo;
    
    @NotBlank(message = "O nome do autor é obrigatório")
    @Size(min = 2, max = 100, message = "O nome do autor deve ter entre 2 e 100 caracteres")
    private String autor;
    
    private Integer anoPublicacao;
    
    @NotNull(message = "A disponibilidade do livro é obrigatória")
    private Boolean disponivel;
    
    private Long categoriaId;
    private String categoriaNome;
}
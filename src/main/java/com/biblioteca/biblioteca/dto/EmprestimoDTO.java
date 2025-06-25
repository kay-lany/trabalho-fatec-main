package com.biblioteca.biblioteca.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmprestimoDTO {
    private Long id;
    
    @NotNull(message = "O ID do usuário é obrigatório")
    private Long usuarioId;
    private String usuarioNome;
    
    @NotNull(message = "O ID do livro é obrigatório")
    private Long livroId;
    private String livroTitulo;
    
    @PastOrPresent(message = "A data de empréstimo não pode ser futura")
    private LocalDate dataEmprestimo;
    
    @FutureOrPresent(message = "A data de devolução prevista não pode ser no passado")
    private LocalDate dataDevolucao;
    
    private Boolean devolvido;
}
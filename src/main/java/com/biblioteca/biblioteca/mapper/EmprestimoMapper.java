package com.biblioteca.biblioteca.mapper;

import com.biblioteca.biblioteca.dto.EmprestimoDTO;
import com.biblioteca.biblioteca.model.Emprestimo;
import com.biblioteca.biblioteca.model.Livro;
import com.biblioteca.biblioteca.model.Usuario;
import com.biblioteca.biblioteca.repository.LivroRepository;
import com.biblioteca.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmprestimoMapper {
    // Precisa dessas duas dependências pq quando estiver convertendo o DTO para
    // o Model, ele vai precisar buscar os objetos reais do usuário e do livro no banco
    // a partir do ID que veio do DTO.
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;

    @Autowired
    public EmprestimoMapper(UsuarioRepository usuarioRepository, LivroRepository livroRepository) {
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
    }

    public EmprestimoDTO toDTO(Emprestimo emprestimo) {
        if (emprestimo == null) {
            return null;
        }

        EmprestimoDTO.EmprestimoDTOBuilder builder = EmprestimoDTO.builder()
                .id(emprestimo.getId())
                .dataEmprestimo(emprestimo.getDataEmprestimo())
                .dataDevolucao(emprestimo.getDataDevolucao())
                .devolvido(emprestimo.getDevolvido());

        if (emprestimo.getUsuario() != null) {
            builder.usuarioId(emprestimo.getUsuario().getId())
                   .usuarioNome(emprestimo.getUsuario().getNome());
        }

        if (emprestimo.getLivro() != null) {
            builder.livroId(emprestimo.getLivro().getId())
                   .livroTitulo(emprestimo.getLivro().getTitulo());
        }

        return builder.build();
    }

    public List<EmprestimoDTO> toDTO(List<Emprestimo> emprestimos) {
        return emprestimos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Emprestimo toEntity(EmprestimoDTO dto) {
        if (dto == null) {
            return null;
        }

        Emprestimo.EmprestimoBuilder builder = Emprestimo.builder()
                .id(dto.getId())
                .dataEmprestimo(dto.getDataEmprestimo())
                .dataDevolucao(dto.getDataDevolucao())
                .devolvido(dto.getDevolvido());

        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId()).orElse(null);
            builder.usuario(usuario);
        }

        if (dto.getLivroId() != null) {
            Livro livro = livroRepository.findById(dto.getLivroId()).orElse(null);
            builder.livro(livro);
        }

        return builder.build();
    }

    public List<Emprestimo> toEntity(List<EmprestimoDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
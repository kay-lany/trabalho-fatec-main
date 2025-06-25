package com.biblioteca.biblioteca.mapper;

import com.biblioteca.biblioteca.dto.LivroDTO;
import com.biblioteca.biblioteca.model.Livro;
import com.biblioteca.biblioteca.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LivroMapper {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public LivroMapper(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public LivroDTO toDTO(Livro livro) {
        if (livro == null) {
            return null;
        }

        LivroDTO.LivroDTOBuilder builder = LivroDTO.builder()
                .id(livro.getId())
                .titulo(livro.getTitulo())
                .autor(livro.getAutor())
                .anoPublicacao(livro.getAnoPublicacao())
                .disponivel(livro.getDisponivel());

        if (livro.getCategoria() != null) {
            builder.categoriaId(livro.getCategoria().getId())
                   .categoriaNome(livro.getCategoria().getNome());
        }

        return builder.build();
    }

    public List<LivroDTO> toDTO(List<Livro> livros) {
        return livros.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Livro toEntity(LivroDTO dto) {
        if (dto == null) {
            return null;
        }

        Livro.LivroBuilder builder = Livro.builder()
                .id(dto.getId())
                .titulo(dto.getTitulo())
                .autor(dto.getAutor())
                .anoPublicacao(dto.getAnoPublicacao())
                .disponivel(dto.getDisponivel());

        if (dto.getCategoriaId() != null) {
            categoriaRepository.findById(dto.getCategoriaId())
                    .ifPresent(builder::categoria);
        }

        return builder.build();
    }

    public List<Livro> toEntity(List<LivroDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
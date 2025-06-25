package com.biblioteca.biblioteca.mapper;

import com.biblioteca.biblioteca.dto.CategoriaDTO;
import com.biblioteca.biblioteca.model.Categoria;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoriaMapper {
    
    public CategoriaDTO toDTO(Categoria categoria) {
        if (categoria == null) {
            return null;
        }
        
        return CategoriaDTO.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .build();
    }
    
    public List<CategoriaDTO> toDTO(List<Categoria> categorias) {
        return categorias.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public Categoria toEntity(CategoriaDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return Categoria.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .build();
    }
    
    public List<Categoria> toEntity(List<CategoriaDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
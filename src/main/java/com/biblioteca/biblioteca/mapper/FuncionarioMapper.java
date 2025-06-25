package com.biblioteca.biblioteca.mapper;

import com.biblioteca.biblioteca.dto.FuncionarioDTO;
import com.biblioteca.biblioteca.model.Funcionario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FuncionarioMapper {
    
    public FuncionarioDTO toDTO(Funcionario funcionario) {
        if (funcionario == null) {
            return null;
        }
        
        return FuncionarioDTO.builder()
                .id(funcionario.getId())
                .nome(funcionario.getNome())
                .cargo(funcionario.getCargo())
                .telefone(funcionario.getTelefone())
                .email(funcionario.getEmail())
                .username(funcionario.getUsername())
                // Não incluir senha no DTO por segurança
                .build();
    }
    
    public List<FuncionarioDTO> toDTO(List<Funcionario> funcionarios) {
        return funcionarios.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public Funcionario toEntity(FuncionarioDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Funcionario.FuncionarioBuilder builder = Funcionario.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .cargo(dto.getCargo())
                .telefone(dto.getTelefone())
                .email(dto.getEmail())
                .username(dto.getUsername());
        
        // Só definir a senha se for informada (pode ser um update sem trocar senha)
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            // Aqui seria ideal implementar um hash de senha
            builder.password(dto.getPassword());
        }
        
        return builder.build();
    }
    
    public List<Funcionario> toEntity(List<FuncionarioDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
    
    // Atualiza um funcionário existente com os dados de um DTO, mantendo 
    // a senha existente caso não seja informada uma nova senha
    public void updateEntityFromDTO(FuncionarioDTO dto, Funcionario funcionario) {
        funcionario.setNome(dto.getNome());
        funcionario.setCargo(dto.getCargo());
        funcionario.setTelefone(dto.getTelefone());
        funcionario.setEmail(dto.getEmail());
        funcionario.setUsername(dto.getUsername());
        
        // Só atualizar a senha se for informada
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            // Aqui seria ideal implementar um hash de senha
            funcionario.setPassword(dto.getPassword());
        }
    }
}
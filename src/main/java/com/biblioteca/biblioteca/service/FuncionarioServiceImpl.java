package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.model.Funcionario;
import com.biblioteca.biblioteca.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    @Autowired
    public FuncionarioServiceImpl(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    @Override
    public Optional<Funcionario> buscarPorId(Long id) {
        return funcionarioRepository.findById(id);
    }

    @Override
    public List<Funcionario> buscarPorNome(String nome) {
        return funcionarioRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Override
    public List<Funcionario> buscarPorCargo(String cargo) {
        return funcionarioRepository.findByCargo(cargo);
    }

    @Override
    public Optional<Funcionario> buscarPorEmail(String email) {
        return funcionarioRepository.findByEmail(email);
    }

    @Override
    public Optional<Funcionario> buscarPorUsername(String username) {
        return funcionarioRepository.findByUsername(username);
    }

    @Override
    public Funcionario salvar(Funcionario funcionario) {
        // Implementar hashing de senha antes de salvar
        // Por enquanto, apenas salvando sem tratamento
        return funcionarioRepository.save(funcionario);
    }

    @Override
    public Funcionario atualizar(Long id, Funcionario funcionario) {
        if (funcionarioRepository.existsById(id)) {
            funcionario.setId(id);
            // Implementar verificação para saber se a senha foi modificada
            return funcionarioRepository.save(funcionario);
        }
        return null; // Tratamento de exceção será adicionado posteriormente
    }

    @Override
    public void deletar(Long id) {
        funcionarioRepository.deleteById(id);
    }

    @Override
    public boolean existePorUsername(String username) {
        return funcionarioRepository.existsByUsername(username);
    }

    @Override
    public boolean existePorEmail(String email) {
        return funcionarioRepository.existsByEmail(email);
    }

    @Override
    public Optional<Funcionario> login(String username, String password) {
        Optional<Funcionario> funcionario = funcionarioRepository.findByUsername(username);
        if (funcionario.isPresent() && funcionario.get().getPassword().equals(password)) {
            return funcionario;
        }
        return Optional.empty();
    }
}
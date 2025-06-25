package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.model.Emprestimo;
import com.biblioteca.biblioteca.model.Livro;
import com.biblioteca.biblioteca.model.Usuario;
import com.biblioteca.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.biblioteca.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmprestimoServiceImpl implements EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;

    @Autowired
    public EmprestimoServiceImpl(EmprestimoRepository emprestimoRepository, LivroRepository livroRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroRepository;
    }

    @Override
    public List<Emprestimo> listarTodos() {
        return emprestimoRepository.findAll();
    }

    @Override
    public Optional<Emprestimo> buscarPorId(Long id) {
        return emprestimoRepository.findById(id);
    }

    @Override
    public List<Emprestimo> buscarPorUsuario(Usuario usuario) {
        return emprestimoRepository.findByUsuario(usuario);
    }

    @Override
    public List<Emprestimo> buscarPorLivro(Livro livro) {
        return emprestimoRepository.findByLivro(livro);
    }

    @Override
    public List<Emprestimo> buscarPorStatus(Boolean devolvido) {
        return emprestimoRepository.findByDevolvido(devolvido);
    }

    @Override
    public List<Emprestimo> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return emprestimoRepository.findByDataEmprestimoBetween(inicio, fim);
    }

    @Override
    public List<Emprestimo> buscarEmprestimosAtivos(Usuario usuario) {
        return emprestimoRepository.findByUsuarioAndDevolvido(usuario, false);
    }

    @Override
    public List<Emprestimo> buscarEmprestimosEmAtraso() {
        LocalDate hoje = LocalDate.now();
        return emprestimoRepository.findByDevolvido(false).stream()
                .filter(emprestimo -> emprestimo.getDataDevolucao() != null && 
                                      emprestimo.getDataDevolucao().isBefore(hoje))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Emprestimo realizarEmprestimo(Emprestimo emprestimo) {
        Livro livro = emprestimo.getLivro();
        
        // Verificar se o livro está disponível
        if (!livro.getDisponivel()) {
            return null; // Tratamento de exceção será adicionado posteriormente
        }
        
        // Definir data de empréstimo como hoje se não for fornecida
        if (emprestimo.getDataEmprestimo() == null) {
            emprestimo.setDataEmprestimo(LocalDate.now());
        }
        
        // Definir status inicial de não devolvido
        emprestimo.setDevolvido(false);
        
        // Atualizar status do livro para indisponível
        livro.setDisponivel(false);
        livroRepository.save(livro);
        
        // Salvar o empréstimo
        return emprestimoRepository.save(emprestimo);
    }

    @Override
    @Transactional
    public Emprestimo realizarDevolucao(Long id) {
        Optional<Emprestimo> emprestimoOpt = emprestimoRepository.findById(id);
        
        if (emprestimoOpt.isPresent()) {
            Emprestimo emprestimo = emprestimoOpt.get();
            
            // Verificar se já foi devolvido
            if (emprestimo.getDevolvido()) {
                return emprestimo; // Já estava devolvido
            }
            
            // Atualizar empréstimo
            emprestimo.setDevolvido(true);
            emprestimo.setDataDevolucao(LocalDate.now());
            
            // Atualizar disponibilidade do livro
            Livro livro = emprestimo.getLivro();
            livro.setDisponivel(true);
            livroRepository.save(livro);
            
            return emprestimoRepository.save(emprestimo);
        }
        
        return null; // Tratamento de exceção será adicionado posteriormente
    }

    @Override
    public Emprestimo atualizar(Long id, Emprestimo emprestimo) {
        if (emprestimoRepository.existsById(id)) {
            emprestimo.setId(id);
            return emprestimoRepository.save(emprestimo);
        }
        return null; // Tratamento de exceção será adicionado posteriormente
    }

    @Override
    public void deletar(Long id) {
        emprestimoRepository.deleteById(id);
    }
}
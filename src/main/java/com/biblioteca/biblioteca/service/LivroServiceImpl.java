package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.model.Livro;
import com.biblioteca.biblioteca.model.Categoria;
import com.biblioteca.biblioteca.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroServiceImpl implements LivroService {

    private final LivroRepository livroRepository;

    @Autowired
    public LivroServiceImpl(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Override
    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.findById(id);
    }

    @Override
    public List<Livro> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    @Override
    public List<Livro> buscarPorAutor(String autor) {
        return livroRepository.findByAutorContainingIgnoreCase(autor);
    }

    @Override
    public List<Livro> buscarPorCategoria(Categoria categoria) {
        return livroRepository.findByCategoria(categoria);
    }

    @Override
    public List<Livro> buscarPorDisponibilidade(Boolean disponivel) {
        return livroRepository.findByDisponivel(disponivel);
    }

    @Override
    public List<Livro> buscarPorAnoPublicacao(Integer ano) {
        return livroRepository.findByAnoPublicacao(ano);
    }

    @Override
    public Livro salvar(Livro livro) {
        return livroRepository.save(livro);
    }

    @Override
    public Livro atualizar(Long id, Livro livro) {
        if (livroRepository.existsById(id)) {
            livro.setId(id);
            return livroRepository.save(livro);
        }
        return null; // Tratamento de exceção será adicionado posteriormente
    }

    @Override
    public void deletar(Long id) {
        livroRepository.deleteById(id);
    }

    @Override
    public Livro alterarDisponibilidade(Long id, Boolean disponivel) {
        Optional<Livro> livroOpt = livroRepository.findById(id);
        if (livroOpt.isPresent()) {
            Livro livro = livroOpt.get();
            livro.setDisponivel(disponivel);
            return livroRepository.save(livro);
        }
        return null; // Tratamento de exceção será adicionado posteriormente
    }
}
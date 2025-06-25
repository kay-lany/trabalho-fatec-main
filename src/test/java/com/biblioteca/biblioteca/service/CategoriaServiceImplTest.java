package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.model.Categoria;
import com.biblioteca.biblioteca.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    private Categoria categoria1;
    private Categoria categoria2;

    @BeforeEach
    void setUp() {
        // Configurar dados de teste
        categoria1 = Categoria.builder()
                .id(1L)
                .nome("Romance")
                .build();

        categoria2 = Categoria.builder()
                .id(2L)
                .nome("Ficção Científica")
                .build();
    }

    @Test
    void listarTodas_DeveRetornarTodasCategorias() {
        // Arrange
        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(categoria1, categoria2));

        // Act
        List<Categoria> resultado = categoriaService.listarTodas();

        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(categoria1));
        assertTrue(resultado.contains(categoria2));
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_QuandoEncontrado_DeveRetornarCategoria() {
        // Arrange
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria1));

        // Act
        Optional<Categoria> resultado = categoriaService.buscarPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(categoria1, resultado.get());
        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_QuandoNaoEncontrado_DeveRetornarVazio() {
        // Arrange
        when(categoriaRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        Optional<Categoria> resultado = categoriaService.buscarPorId(3L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(categoriaRepository, times(1)).findById(3L);
    }

    @Test
    void buscarPorNome_QuandoEncontrado_DeveRetornarCategoria() {
        // Arrange
        when(categoriaRepository.findByNome("Romance")).thenReturn(Optional.of(categoria1));

        // Act
        Optional<Categoria> resultado = categoriaService.buscarPorNome("Romance");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(categoria1, resultado.get());
        verify(categoriaRepository, times(1)).findByNome("Romance");
    }

    @Test
    void salvar_DeveSalvarCategoria() {
        // Arrange
        Categoria novaCategoria = Categoria.builder().nome("Terror").build();
        Categoria categoriaSalva = Categoria.builder().id(3L).nome("Terror").build();
        
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaSalva);

        // Act
        Categoria resultado = categoriaService.salvar(novaCategoria);

        // Assert
        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals("Terror", resultado.getNome());
        verify(categoriaRepository, times(1)).save(novaCategoria);
    }

    @Test
    void atualizar_QuandoExiste_DeveAtualizarCategoria() {
        // Arrange
        Long id = 1L;
        Categoria categoriaAtualizada = Categoria.builder().nome("Romance Atualizado").build();
        Categoria categoriaRetornada = Categoria.builder().id(id).nome("Romance Atualizado").build();
        
        when(categoriaRepository.existsById(id)).thenReturn(true);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaRetornada);

        // Act
        Categoria resultado = categoriaService.atualizar(id, categoriaAtualizada);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Romance Atualizado", resultado.getNome());
        verify(categoriaRepository, times(1)).existsById(id);
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void atualizar_QuandoNaoExiste_DeveRetornarNull() {
        // Arrange
        Long id = 5L;
        Categoria categoriaAtualizada = Categoria.builder().nome("Nova Categoria").build();
        
        when(categoriaRepository.existsById(id)).thenReturn(false);

        // Act
        Categoria resultado = categoriaService.atualizar(id, categoriaAtualizada);

        // Assert
        assertNull(resultado);
        verify(categoriaRepository, times(1)).existsById(id);
        verify(categoriaRepository, times(0)).save(any(Categoria.class));
    }

    @Test
    void deletar_DeveChamarDeleteById() {
        // Arrange
        Long id = 1L;
        doNothing().when(categoriaRepository).deleteById(id);

        // Act
        categoriaService.deletar(id);

        // Assert
        verify(categoriaRepository, times(1)).deleteById(id);
    }

    @Test
    void existePorNome_QuandoExiste_DeveRetornarTrue() {
        // Arrange
        when(categoriaRepository.existsByNome("Romance")).thenReturn(true);

        // Act
        boolean resultado = categoriaService.existePorNome("Romance");

        // Assert
        assertTrue(resultado);
        verify(categoriaRepository, times(1)).existsByNome("Romance");
    }

    @Test
    void existePorNome_QuandoNaoExiste_DeveRetornarFalse() {
        // Arrange
        when(categoriaRepository.existsByNome("Não Existe")).thenReturn(false);

        // Act
        boolean resultado = categoriaService.existePorNome("Não Existe");

        // Assert
        assertFalse(resultado);
        verify(categoriaRepository, times(1)).existsByNome("Não Existe");
    }
}
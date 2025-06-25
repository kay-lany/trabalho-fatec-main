package com.biblioteca.biblioteca.controller;

import com.biblioteca.biblioteca.dto.CategoriaDTO;
import com.biblioteca.biblioteca.mapper.CategoriaMapper;
import com.biblioteca.biblioteca.model.Categoria;
import com.biblioteca.biblioteca.service.CategoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CategoriaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoriaService categoriaService;

    @Mock
    private CategoriaMapper categoriaMapper;

    @InjectMocks
    private CategoriaController categoriaController;

    private ObjectMapper objectMapper;
    private Categoria categoria1;
    private Categoria categoria2;
    private CategoriaDTO categoriaDTO1;
    private CategoriaDTO categoriaDTO2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoriaController).build();
        objectMapper = new ObjectMapper();

        // Configurar dados de teste - Entidades
        categoria1 = Categoria.builder()
                .id(1L)
                .nome("Romance")
                .build();

        categoria2 = Categoria.builder()
                .id(2L)
                .nome("Ficção Científica")
                .build();

        // Configurar dados de teste - DTOs
        categoriaDTO1 = CategoriaDTO.builder()
                .id(1L)
                .nome("Romance")
                .build();

        categoriaDTO2 = CategoriaDTO.builder()
                .id(2L)
                .nome("Ficção Científica")
                .build();
    }

    @Test
    void listarTodas_DeveRetornarTodasCategoriasDTO() throws Exception {
        // Arrange
        when(categoriaService.listarTodas()).thenReturn(Arrays.asList(categoria1, categoria2));
        when(categoriaMapper.toDTO(Arrays.asList(categoria1, categoria2)))
                .thenReturn(Arrays.asList(categoriaDTO1, categoriaDTO2));

        // Act & Assert
        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nome", is("Romance")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nome", is("Ficção Científica")));

        verify(categoriaService, times(1)).listarTodas();
        verify(categoriaMapper, times(1)).toDTO(Arrays.asList(categoria1, categoria2));
    }

    @Test
    void buscarPorId_QuandoEncontrado_DeveRetornarCategoriaDTO() throws Exception {
        // Arrange
        when(categoriaService.buscarPorId(1L)).thenReturn(Optional.of(categoria1));
        when(categoriaMapper.toDTO(categoria1)).thenReturn(categoriaDTO1);

        // Act & Assert
        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Romance")));

        verify(categoriaService, times(1)).buscarPorId(1L);
        verify(categoriaMapper, times(1)).toDTO(categoria1);
    }

    @Test
    void buscarPorId_QuandoNaoEncontrado_DeveRetornarNotFound() throws Exception {
        // Arrange
        when(categoriaService.buscarPorId(3L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/categorias/3"))
                .andExpect(status().isNotFound());

        verify(categoriaService, times(1)).buscarPorId(3L);
    }

    @Test
    void criar_QuandoValido_DeveCriarCategoria() throws Exception {
        // Arrange
        CategoriaDTO novaCategoriaDTO = CategoriaDTO.builder().nome("Terror").build();
        Categoria novaCategoria = Categoria.builder().nome("Terror").build();
        Categoria categoriaSalva = Categoria.builder().id(3L).nome("Terror").build();
        CategoriaDTO categoriaSalvaDTO = CategoriaDTO.builder().id(3L).nome("Terror").build();

        when(categoriaService.existePorNome("Terror")).thenReturn(false);
        when(categoriaMapper.toEntity(novaCategoriaDTO)).thenReturn(novaCategoria);
        when(categoriaService.salvar(novaCategoria)).thenReturn(categoriaSalva);
        when(categoriaMapper.toDTO(categoriaSalva)).thenReturn(categoriaSalvaDTO);

        // Act & Assert
        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novaCategoriaDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.nome", is("Terror")));

        verify(categoriaService, times(1)).existePorNome("Terror");
        verify(categoriaMapper, times(1)).toEntity(novaCategoriaDTO);
        verify(categoriaService, times(1)).salvar(novaCategoria);
        verify(categoriaMapper, times(1)).toDTO(categoriaSalva);
    }

    @Test
    void criar_QuandoNomeJaExiste_DeveRetornarConflict() throws Exception {
        // Arrange
        CategoriaDTO novaCategoriaDTO = CategoriaDTO.builder().nome("Romance").build();

        when(categoriaService.existePorNome("Romance")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novaCategoriaDTO)))
                .andExpect(status().isConflict());

        verify(categoriaService, times(1)).existePorNome("Romance");
        verify(categoriaMapper, never()).toEntity(any(CategoriaDTO.class));
        verify(categoriaService, never()).salvar(any(Categoria.class));
    }

    @Test
    void atualizar_QuandoValido_DeveAtualizarCategoria() throws Exception {
        // Arrange
        Long id = 1L;
        CategoriaDTO atualizacaoDTO = CategoriaDTO.builder().nome("Romance Atualizado").build();
        Categoria atualizacao = Categoria.builder().nome("Romance Atualizado").build();
        Categoria categoriaAtualizada = Categoria.builder().id(id).nome("Romance Atualizado").build();
        CategoriaDTO categoriaAtualizadaDTO = CategoriaDTO.builder().id(id).nome("Romance Atualizado").build();

        when(categoriaService.buscarPorId(id)).thenReturn(Optional.of(categoria1));
        when(categoriaService.buscarPorNome("Romance Atualizado")).thenReturn(Optional.empty());
        when(categoriaMapper.toEntity(atualizacaoDTO)).thenReturn(atualizacao);
        when(categoriaService.atualizar(eq(id), any(Categoria.class))).thenReturn(categoriaAtualizada);
        when(categoriaMapper.toDTO(categoriaAtualizada)).thenReturn(categoriaAtualizadaDTO);

        // Act & Assert
        mockMvc.perform(put("/api/categorias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atualizacaoDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Romance Atualizado")));

        verify(categoriaService, times(1)).buscarPorId(id);
        verify(categoriaService, times(1)).buscarPorNome("Romance Atualizado");
        verify(categoriaMapper, times(1)).toEntity(atualizacaoDTO);
        verify(categoriaService, times(1)).atualizar(eq(id), any(Categoria.class));
        verify(categoriaMapper, times(1)).toDTO(categoriaAtualizada);
    }

    @Test
    void deletar_QuandoExiste_DeveRemoverCategoria() throws Exception {
        // Arrange
        Long id = 1L;
        when(categoriaService.buscarPorId(id)).thenReturn(Optional.of(categoria1));
        doNothing().when(categoriaService).deletar(id);

        // Act & Assert
        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());

        verify(categoriaService, times(1)).buscarPorId(id);
        verify(categoriaService, times(1)).deletar(id);
    }

    @Test
    void deletar_QuandoNaoExiste_DeveRetornarNotFound() throws Exception {
        // Arrange
        Long id = 5L;
        when(categoriaService.buscarPorId(id)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/categorias/5"))
                .andExpect(status().isNotFound());

        verify(categoriaService, times(1)).buscarPorId(id);
        verify(categoriaService, never()).deletar(anyLong());
    }
}
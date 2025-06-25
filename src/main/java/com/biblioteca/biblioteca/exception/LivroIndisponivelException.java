package com.biblioteca.biblioteca.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class LivroIndisponivelException extends RuntimeException {
    
    public LivroIndisponivelException(String message) {
        super(message);
    }
    
    public LivroIndisponivelException(Long livroId) {
        super(String.format("Livro com ID: '%s' não está disponível para empréstimo", livroId));
    }
}
package br.com.fiap.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Long id) {
        super("Não foi possível encontrar o recurso com o ID: " + id);
    }
}


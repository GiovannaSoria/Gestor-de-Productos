package com.gestorproductos.inventario.exception;

public class NotFoundException extends RuntimeException {

    private final String resourceType;
    private final String identifier;

    public NotFoundException(String resourceType, String identifier) {
        super();
        this.resourceType = resourceType;
        this.identifier = identifier;
    }

    @Override
    public String getMessage() {
        return String.format("El recurso '%s' con identificador '%s' no fue encontrado", resourceType, identifier);
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getIdentifier() {
        return identifier;
    }
} 
package com.gestorproductos.inventario.exception;

public class DuplicateResourceException extends RuntimeException {

    private final String resourceType;
    private final String duplicateValue;

    public DuplicateResourceException(String resourceType, String duplicateValue) {
        super();
        this.resourceType = resourceType;
        this.duplicateValue = duplicateValue;
    }

    @Override
    public String getMessage() {
        return String.format("Ya existe un %s con el valor '%s'", resourceType, duplicateValue);
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getDuplicateValue() {
        return duplicateValue;
    }
} 
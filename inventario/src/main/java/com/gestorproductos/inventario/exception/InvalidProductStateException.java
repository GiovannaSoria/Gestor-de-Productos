package com.gestorproductos.inventario.exception;

public class InvalidProductStateException extends RuntimeException {

    private final String estadoActual;
    private final String estadoSolicitado;

    public InvalidProductStateException(String estadoActual, String estadoSolicitado) {
        super();
        this.estadoActual = estadoActual;
        this.estadoSolicitado = estadoSolicitado;
    }

    public InvalidProductStateException(String mensaje) {
        super();
        this.estadoActual = null;
        this.estadoSolicitado = mensaje;
    }

    @Override
    public String getMessage() {
        if (estadoActual != null) {
            return String.format("No se puede cambiar del estado '%s' al estado '%s'", estadoActual, estadoSolicitado);
        }
        return String.format("Estado de producto inválido: %s", estadoSolicitado);
    }

    public String getEstadoActual() {
        return estadoActual;
    }

    public String getEstadoSolicitado() {
        return estadoSolicitado;
    }
} 
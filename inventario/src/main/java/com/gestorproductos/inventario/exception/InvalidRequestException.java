package com.gestorproductos.inventario.exception;

public class InvalidRequestException extends RuntimeException {

    private final String campo;
    private final String valorRecibido;
    private final String valorEsperado;

    public InvalidRequestException(String campo, String valorRecibido, String valorEsperado) {
        super();
        this.campo = campo;
        this.valorRecibido = valorRecibido;
        this.valorEsperado = valorEsperado;
    }

    public InvalidRequestException(String campo, String mensaje) {
        super();
        this.campo = campo;
        this.valorRecibido = null;
        this.valorEsperado = mensaje;
    }

    @Override
    public String getMessage() {
        if (valorRecibido != null) {
            return String.format("Valor inválido para '%s'. Recibido: '%s', Esperado: %s", 
                campo, valorRecibido, valorEsperado);
        }
        return String.format("Campo '%s' inválido: %s", campo, valorEsperado);
    }

    public String getCampo() {
        return campo;
    }

    public String getValorRecibido() {
        return valorRecibido;
    }

    public String getValorEsperado() {
        return valorEsperado;
    }
} 
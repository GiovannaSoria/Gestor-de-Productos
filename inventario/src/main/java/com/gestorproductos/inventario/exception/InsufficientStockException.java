package com.gestorproductos.inventario.exception;

public class InsufficientStockException extends RuntimeException {

    private final Integer stockActual;
    private final Integer cantidadSolicitada;

    public InsufficientStockException(Integer stockActual, Integer cantidadSolicitada) {
        super();
        this.stockActual = stockActual;
        this.cantidadSolicitada = cantidadSolicitada;
    }

    @Override
    public String getMessage() {
        return String.format("Stock insuficiente. Disponible: %d, Solicitado: %d", stockActual, cantidadSolicitada);
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public Integer getCantidadSolicitada() {
        return cantidadSolicitada;
    }
} 
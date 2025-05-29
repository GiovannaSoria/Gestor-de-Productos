package com.gestorproductos.inventario.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestorproductos.inventario.exception.InsufficientStockException;
import com.gestorproductos.inventario.exception.InvalidProductStateException;
import com.gestorproductos.inventario.exception.InvalidRequestException;
import com.gestorproductos.inventario.exception.NotFoundException;
import com.gestorproductos.inventario.model.Producto;
import com.gestorproductos.inventario.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Integer id) {
        Producto producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(producto);
    }

    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodosProductos(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer stockMinimo) {
        
        if (estado != null && !estado.trim().isEmpty()) {
            List<Producto> productos = productoService.obtenerPorEstado(estado.trim());
            return ResponseEntity.ok(productos);
        }
        
        if (stockMinimo != null) {
            List<Producto> productos = productoService.obtenerProductosConStockBajo(stockMinimo);
            return ResponseEntity.ok(productos);
        }
        
        List<Producto> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productos);
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto productoCreado = productoService.crear(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoCreado);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Producto> cambiarEstadoProducto(
            @PathVariable Integer id, 
            @RequestBody EstadoRequest estadoRequest) {
        Producto producto = productoService.cambiarEstado(id, estadoRequest.getEstado());
        return ResponseEntity.ok(producto);
    }

    @PatchMapping("/{id}/stock/aumentar")
    public ResponseEntity<Producto> aumentarStock(
            @PathVariable Integer id, 
            @RequestBody StockRequest stockRequest) {
        Producto producto = productoService.aumentarStock(
            id, 
            stockRequest.getCantidad(), 
            stockRequest.getCostoCompra()
        );
        return ResponseEntity.ok(producto);
    }

    @PatchMapping("/{id}/stock/disminuir")
    public ResponseEntity<Producto> disminuirStock(
            @PathVariable Integer id, 
            @RequestBody CantidadRequest cantidadRequest) {
        Producto producto = productoService.disminuirStock(id, cantidadRequest.getCantidad());
        return ResponseEntity.ok(producto);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<String> manejarNoEncontrado(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({InvalidRequestException.class})
    public ResponseEntity<String> manejarSolicitudInvalida(InvalidRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler({InvalidProductStateException.class})
    public ResponseEntity<String> manejarEstadoInvalido(InvalidProductStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler({InsufficientStockException.class})
    public ResponseEntity<String> manejarStockInsuficiente(InsufficientStockException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> manejarErrorGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + ex.getMessage());
    }

    public static class EstadoRequest {
        private String estado;

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }
    }

    public static class StockRequest {
        private Integer cantidad;
        private BigDecimal costoCompra;

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }

        public BigDecimal getCostoCompra() {
            return costoCompra;
        }

        public void setCostoCompra(BigDecimal costoCompra) {
            this.costoCompra = costoCompra;
        }
    }

    public static class CantidadRequest {
        private Integer cantidad;

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
    }
} 
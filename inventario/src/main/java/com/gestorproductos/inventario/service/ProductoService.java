package com.gestorproductos.inventario.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestorproductos.inventario.exception.InsufficientStockException;
import com.gestorproductos.inventario.exception.InvalidProductStateException;
import com.gestorproductos.inventario.exception.InvalidRequestException;
import com.gestorproductos.inventario.exception.NotFoundException;
import com.gestorproductos.inventario.model.Producto;
import com.gestorproductos.inventario.repository.ProductoRepository;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private static final BigDecimal MARGEN_GANANCIA = new BigDecimal("0.25");

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public Producto obtenerPorId(Integer id) {
        Optional<Producto> producto = productoRepository.findById(id);
        if (producto.isEmpty()) {
            throw new NotFoundException("Producto", id.toString());
        }
        return producto.get();
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Producto crear(Producto producto) {
        validarDatosBasicosProducto(producto);

        if (producto.getStockActual() == null || producto.getStockActual() < 0) {
            throw new InvalidRequestException("stockActual", "debe ser mayor o igual a 0");
        }

        if (producto.getPrecioVenta() == null || producto.getPrecioVenta().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("precioVenta", "debe ser mayor a 0");
        }

        if (producto.getEstadoProducto() == null || producto.getEstadoProducto().trim().isEmpty()) {
            producto.setEstadoProducto("Activo");
        }

        validarEstadoProducto(producto.getEstadoProducto());
        actualizarEstadoPorStock(producto);

        return productoRepository.save(producto);
    }

    public Producto cambiarEstado(Integer id, String nuevoEstado) {
        Producto producto = obtenerPorId(id);
        
        validarEstadoProducto(nuevoEstado);
        
        if (producto.getEstadoProducto().equals(nuevoEstado)) {
            throw new InvalidProductStateException(producto.getEstadoProducto(), nuevoEstado);
        }

        producto.setEstadoProducto(nuevoEstado);
        
        if ("Agotado".equals(nuevoEstado)) {
            producto.setStockActual(0);
        }

        return productoRepository.save(producto);
    }

    public Producto aumentarStock(Integer id, Integer cantidad, BigDecimal nuevoCostoCompra) {
        Producto producto = obtenerPorId(id);

        if (cantidad == null || cantidad <= 0) {
            throw new InvalidRequestException("cantidad", "debe ser mayor a 0");
        }

        if (nuevoCostoCompra == null || nuevoCostoCompra.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("costoCompra", "debe ser mayor a 0");
        }

        Integer stockAnterior = producto.getStockActual();
        BigDecimal costoAnterior = producto.getCostoCompra() != null ? producto.getCostoCompra() : BigDecimal.ZERO;

        BigDecimal costoTotalAnterior = costoAnterior.multiply(new BigDecimal(stockAnterior));
        BigDecimal costoTotalNuevo = nuevoCostoCompra.multiply(new BigDecimal(cantidad));
        BigDecimal costoTotalFinal = costoTotalAnterior.add(costoTotalNuevo);

        Integer stockFinal = stockAnterior + cantidad;
        BigDecimal costoPromedioFinal = costoTotalFinal.divide(new BigDecimal(stockFinal), 2, RoundingMode.HALF_UP);

        BigDecimal nuevoPrecioVenta = costoPromedioFinal.multiply(BigDecimal.ONE.add(MARGEN_GANANCIA))
                .setScale(2, RoundingMode.HALF_UP);

        producto.setStockActual(stockFinal);
        producto.setCostoCompra(costoPromedioFinal);
        producto.setPrecioVenta(nuevoPrecioVenta);
        producto.setEstadoProducto("Activo");

        return productoRepository.save(producto);
    }

    public Producto disminuirStock(Integer id, Integer cantidad) {
        Producto producto = obtenerPorId(id);

        if (cantidad == null || cantidad <= 0) {
            throw new InvalidRequestException("cantidad", "debe ser mayor a 0");
        }

        if (producto.getStockActual() < cantidad) {
            throw new InsufficientStockException(producto.getStockActual(), cantidad);
        }

        Integer nuevoStock = producto.getStockActual() - cantidad;
        producto.setStockActual(nuevoStock);

        actualizarEstadoPorStock(producto);

        return productoRepository.save(producto);
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerPorEstado(String estado) {
        validarEstadoProducto(estado);
        return productoRepository.findByEstadoProducto(estado);
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosConStockBajo(Integer limiteMinimoStock) {
        if (limiteMinimoStock == null || limiteMinimoStock < 0) {
            throw new InvalidRequestException("limiteMinimoStock", "debe ser mayor o igual a 0");
        }
        return productoRepository.findByStockActualLessThan(limiteMinimoStock);
    }

    private void validarDatosBasicosProducto(Producto producto) {
        if (producto.getNombreProducto() == null || producto.getNombreProducto().trim().isEmpty()) {
            throw new InvalidRequestException("nombreProducto", "es obligatorio");
        }
        producto.setNombreProducto(producto.getNombreProducto().trim());
    }

    private void validarEstadoProducto(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new InvalidRequestException("estadoProducto", "es obligatorio");
        }

        String estadoLimpio = estado.trim();
        if (!estadoLimpio.equals("Activo") && !estadoLimpio.equals("Inactivo") && !estadoLimpio.equals("Agotado")) {
            throw new InvalidProductStateException("Los estados válidos son: Activo, Inactivo, Agotado");
        }
    }

    private void actualizarEstadoPorStock(Producto producto) {
        if (producto.getStockActual() == 0) {
            producto.setEstadoProducto("Agotado");
        } else if (!"Inactivo".equals(producto.getEstadoProducto())) {
            producto.setEstadoProducto("Activo");
        }
    }
} 
package com.gestorproductos.inventario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestorproductos.inventario.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
    List<Producto> findByEstadoProducto(String estadoProducto);
    
    Optional<Producto> findByNombreProducto(String nombreProducto);
    
    List<Producto> findByStockActualLessThan(Integer stock);
    
    List<Producto> findByEstadoProductoAndStockActualGreaterThan(String estadoProducto, Integer stock);
} 
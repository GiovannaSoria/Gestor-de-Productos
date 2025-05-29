package com.gestorproductos.inventario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestorproductos.inventario.model.CategoriaProducto;

@Repository
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Integer> {
    
    Optional<CategoriaProducto> findByNombreCategoria(String nombreCategoria);
    
    boolean existsByNombreCategoria(String nombreCategoria);
} 
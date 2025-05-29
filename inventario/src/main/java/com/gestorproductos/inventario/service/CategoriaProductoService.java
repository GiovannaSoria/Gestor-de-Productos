package com.gestorproductos.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestorproductos.inventario.exception.DuplicateResourceException;
import com.gestorproductos.inventario.exception.InvalidRequestException;
import com.gestorproductos.inventario.exception.NotFoundException;
import com.gestorproductos.inventario.model.CategoriaProducto;
import com.gestorproductos.inventario.repository.CategoriaProductoRepository;

@Service
@Transactional
public class CategoriaProductoService {

    private final CategoriaProductoRepository categoriaProductoRepository;

    public CategoriaProductoService(CategoriaProductoRepository categoriaProductoRepository) {
        this.categoriaProductoRepository = categoriaProductoRepository;
    }

    public CategoriaProducto crear(CategoriaProducto categoria) {
        if (categoria.getNombreCategoria() == null || categoria.getNombreCategoria().trim().isEmpty()) {
            throw new InvalidRequestException("nombreCategoria", "es obligatorio");
        }

        if (categoriaProductoRepository.existsByNombreCategoria(categoria.getNombreCategoria())) {
            throw new DuplicateResourceException("categoría", categoria.getNombreCategoria());
        }

        categoria.setNombreCategoria(categoria.getNombreCategoria().trim());
        return categoriaProductoRepository.save(categoria);
    }

    @Transactional(readOnly = true)
    public CategoriaProducto obtenerPorId(Integer id) {
        Optional<CategoriaProducto> categoria = categoriaProductoRepository.findById(id);
        if (categoria.isEmpty()) {
            throw new NotFoundException("Categoría", id.toString());
        }
        return categoria.get();
    }

    @Transactional(readOnly = true)
    public List<CategoriaProducto> obtenerTodas() {
        return categoriaProductoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CategoriaProducto obtenerPorNombre(String nombreCategoria) {
        Optional<CategoriaProducto> categoria = categoriaProductoRepository.findByNombreCategoria(nombreCategoria);
        if (categoria.isEmpty()) {
            throw new NotFoundException("Categoría", nombreCategoria);
        }
        return categoria.get();
    }

    public CategoriaProducto actualizar(Integer id, CategoriaProducto categoriaActualizada) {
        CategoriaProducto categoriaExistente = obtenerPorId(id);

        if (categoriaActualizada.getNombreCategoria() == null || 
            categoriaActualizada.getNombreCategoria().trim().isEmpty()) {
            throw new InvalidRequestException("nombreCategoria", "es obligatorio");
        }

        String nuevoNombre = categoriaActualizada.getNombreCategoria().trim();
        if (!categoriaExistente.getNombreCategoria().equals(nuevoNombre) && 
            categoriaProductoRepository.existsByNombreCategoria(nuevoNombre)) {
            throw new DuplicateResourceException("categoría", nuevoNombre);
        }

        categoriaExistente.setNombreCategoria(nuevoNombre);
        categoriaExistente.setDescripcion(categoriaActualizada.getDescripcion());

        return categoriaProductoRepository.save(categoriaExistente);
    }

    public void eliminar(Integer id) {
        CategoriaProducto categoria = obtenerPorId(id);
        categoriaProductoRepository.delete(categoria);
    }
} 
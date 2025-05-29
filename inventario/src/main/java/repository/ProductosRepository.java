package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Productos;

public interface ProductosRepository extends JpaRepository<Productos, String> {

} 
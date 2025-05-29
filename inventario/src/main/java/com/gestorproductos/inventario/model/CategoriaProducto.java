package com.gestorproductos.inventario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "categorias_producto")
public class CategoriaProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @Column(name = "nombre_categoria", length = 100, nullable = false, unique = true)
    private String nombreCategoria;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Version
    private Long version;

    public CategoriaProducto() {
    }

    public CategoriaProducto(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idCategoria == null) ? 0 : idCategoria.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CategoriaProducto other = (CategoriaProducto) obj;
        if (idCategoria == null) {
            if (other.idCategoria != null)
                return false;
        } else if (!idCategoria.equals(other.idCategoria))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CategoriaProducto [idCategoria=" + idCategoria + ", nombreCategoria=" + nombreCategoria
                + ", descripcion=" + descripcion + ", version=" + version + "]";
    }
} 
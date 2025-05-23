package org.ngarcia.webapp.jsf3.entities;

import jakarta.inject.Named;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(name="productos")
public class Producto {

   @Id
   @GeneratedValue( strategy = GenerationType.IDENTITY)
   private Long id;

   @NotEmpty(message = "Nombre no puede ser vacío")
   private String nombre;

   @NotNull(message = "Precio no puede ser vacío")
   @Min(value = 5, message = "Precio debe ser mayor que 4")
   @Max(value = 100000, message = "Precio no puede ser mayor que 100000")
   private Integer precio;

   @NotEmpty(message = "Sku no puede ser vacío")
   @Size(min = 4, max = 10, message = "Tamaño debe ser de 4 a 10")
   private String sku;

   @NotNull(message = "Fecha no puede ser vacía")
   @Column(name="fecha_registro")
   private LocalDate fechaRegistro;

   @NotNull(message = "Falta categoría")
   @ManyToOne(fetch = FetchType.LAZY)
   private Categoria categoria;

   public Producto() {
   }

   public Producto(String nombre) {
      this.nombre = nombre;
   }

   public String getNombre() {
      return nombre;
   }

   public void setNombre(String nombre) {
      this.nombre = nombre;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Integer getPrecio() {
      return precio;
   }

   public void setPrecio(Integer precio) {
      this.precio = precio;
   }

   public String getSku() {
      return sku;
   }

   public void setSku(String sku) {
      this.sku = sku;
   }

   public LocalDate getFechaRegistro() {
      return fechaRegistro;
   }

   public void setFechaRegistro(LocalDate fechaRegistro) {
      this.fechaRegistro = fechaRegistro;
   }

   public Categoria getCategoria() {
      return categoria;
   }

   public void setCategoria(Categoria categoria) {
      this.categoria = categoria;
   }

   //Se comenta porque se va a asignar desde el formulario
   //@PrePersist
   public void prePersist() {
      fechaRegistro = LocalDate.now();
   }

   @Override
   public String toString() {
      return "{" +
              "id=" + id +
              ", nombre='" + nombre + '\'' +
              ", precio=" + precio +
              ", sku='" + sku + '\'' +
              ", fechaRegistro=" + fechaRegistro +
              '}';
   }

   public boolean isEmpty() {
      return (nombre == null || nombre.isEmpty()) &&   // String @NotEmpty
              precio == null &&                         // Integer @NotNull
              (sku == null || sku.isEmpty()) &&         // String @NotEmpty
              fechaRegistro == null &&                  // LocalDate @NotNull
              categoria == null;                        // Categoria @NotNull
   }
}

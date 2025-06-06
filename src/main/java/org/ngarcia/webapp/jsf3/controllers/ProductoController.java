package org.ngarcia.webapp.jsf3.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.*;
import jakarta.enterprise.inject.*;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.ngarcia.webapp.jsf3.entities.Categoria;
import org.ngarcia.webapp.jsf3.entities.Producto;
import org.ngarcia.webapp.jsf3.services.ProductoService;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Model //Model equivale a Named + RequestScoped
public class ProductoController {

   private Producto producto;

   private Long id;

   private List<Producto> listado;

   @Inject
   private ProductoService service;

   @Inject
   private FacesContext facesContext;

   @Inject
   private ResourceBundle bundle;

   private String textoBuscar;

   @PostConstruct
   public void init() {
      this.listado = service.listar();
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public List<Producto> getListado() {
      return listado;
   }

   public void setListado(List<Producto> listado) {
      this.listado = listado;
   }

   public String getTextoBuscar() {
      return textoBuscar;
   }

   public void setTextoBuscar(String textoBuscar) {
      this.textoBuscar = textoBuscar;
   }

   @Produces
   @Model
   public String titulo() {
      //return "Hola mundo JavaServer Faces 3.0 desde controller";
      return bundle.getString("producto.index.titulo");
   }

   @Produces
   @Model
   public Producto producto() {
      //this.producto = new Producto();
      if (this.producto == null) {
         this.producto = new Producto();
         if (this.id != null && this.id > 0) {
            service.porId(id).ifPresent(p -> this.producto = p);
         }
      }
      //System.out.println("PRODUCTO COMUN:" + producto);
      return this.producto;
   }

   @Produces
   @RequestScoped
   @Named("listadoCategorias")
   public List<Categoria> findAllCategorias() {
      return service.listarCategorias();
   }

   public String guardar() {

      System.out.println("GUARDAR: "+this.producto);

      System.out.println(this.producto);
      if(this.producto.getId() != null && this.producto.getId() > 0) {
         facesContext.addMessage(null,new FacesMessage(
                 String.format(bundle.getString("producto.mensaje.editar"),producto.getNombre())));
      }
      else {
         facesContext.addMessage(null,new FacesMessage(
                 String.format(bundle.getString("producto.mensaje.crear"),producto.getNombre())));
      }

      service.guardar(this.producto);

      this.listado = service.listar();

      //el redirect es importante para evitar que se ejecute más de una vez (creo)
      //return "indexNoModal.xhtml?faces-redirect=true";
      //cuando emprezamos a utilizar la validación con ajax lo cambiamos
      return "indexNoModal.xhtml";
   }

   public String editar(Long id) {
      this.id = id;
      // Quita el faces-redirect para mantener el ID durante validaciones
      return "productoForm.xhtml";
   }

   //public String eliminar(Producto producto) {
   public void eliminar(Producto producto) {

      service.eliminar(producto.getId());

      facesContext.addMessage(null,new FacesMessage(
              bundle.getString("producto.mensaje.eliminar"),producto.getNombre()));

      this.listado = service.listar();

      //return "indexNoModal.xhtml?faces-redirect=true";
      //cuando emprezamos a utilizar la validación con ajax lo cambiamos
      //return "indexNoModal.xhtml";
   }

   public void buscar() {
      this.listado = service.buscarPorNombre(this.textoBuscar);
   }

   public String getDescButton() {

      String idParam = "";

      if(this.id != null) { //lo utiliza el primer botón de Editar (se pierde si hay error de validación)
         idParam = this.id.toString();
      }
      else { //lo utiliza el segundo botón de Editar
         FacesContext facesContext = FacesContext.getCurrentInstance();
         Map<String, String> params = facesContext.getExternalContext()
                 .getRequestParameterMap();
         idParam = params.get("id");
      }

      System.out.println("ID:"+idParam);

      if (idParam != null && !idParam.isEmpty()) {
         return bundle.getString("producto.boton.editar");
      }
      else {
         return bundle.getString("producto.boton.crear");
      }
   }
}

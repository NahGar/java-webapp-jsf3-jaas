package org.ngarcia.webapp.jsf3.services;

import jakarta.annotation.Resource;
import jakarta.annotation.security.*;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.ngarcia.webapp.jsf3.entities.*;
import org.ngarcia.webapp.jsf3.repositories.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Stateless
@DeclareRoles({"USER","ADMIN"})
public class ProductoServiceImpl implements ProductoService {

   @Inject
   //private CrudRepository<Producto> repository;
   private ProductoRepository repository;

   @Inject
   private CrudRepository<Categoria> repositoryCategoria;

   @Resource
   private SessionContext ctx;

   @Override
   @PermitAll //es público
   public List<Producto> listar() {

      Principal usuario = ctx.getCallerPrincipal();
      String username = usuario.getName();
      System.out.println("username:"+username);
      if(ctx.isCallerInRole("ADMIN")) {
         System.out.println("ES ADMIN");
      } else if (ctx.isCallerInRole("USER")) {
         System.out.println("ES USER");
      }
      else {
         System.out.println("ES ANONIMO");
         //throw new SecurityException("Tiene que loguearse");
      }

      return repository.listar();
   }

   @Override
   @RolesAllowed({"USER","ADMIN"})
   public Optional<Producto> porId(Long id) {
      return Optional.ofNullable(repository.porId(id));
   }

   @Override
   @RolesAllowed({"ADMIN"})
   public void guardar(Producto producto) {
      repository.guardar(producto);
   }

   @Override
   @RolesAllowed({"ADMIN"})
   public void eliminar(Long id) {
      repository.eliminar(id);
   }

   @Override
   @RolesAllowed({"USER","ADMIN"})
   public List<Categoria> listarCategorias() {
      return repositoryCategoria.listar();
   }

   @Override
   @RolesAllowed({"USER","ADMIN"})
   public Optional<Categoria> porIdCategoria(Long id) {
      return Optional.ofNullable(repositoryCategoria.porId(id));
   }

   @Override
   @RolesAllowed({"USER","ADMIN"})
   public List<Producto> buscarPorNombre(String nombre) {
      return repository.buscarPorNombre(nombre);
   }
}

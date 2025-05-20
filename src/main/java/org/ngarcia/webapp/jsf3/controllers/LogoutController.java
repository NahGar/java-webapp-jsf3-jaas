package org.ngarcia.webapp.jsf3.controllers;

import jakarta.enterprise.inject.Model;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ResourceBundle;

@Model
public class LogoutController {

    @Inject
    private FacesContext facesContext; //ProducerResources

    @Inject
    private ResourceBundle bundle;

    public String logout() throws ServletException {

        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        request.logout();
        request.getSession().invalidate(); //elimina info en la sesión
        facesContext.addMessage(null, new FacesMessage());
        facesContext.addMessage(null,new FacesMessage(bundle.getString("login.texto.logout")));
        return "login.xhtml";
    }

}

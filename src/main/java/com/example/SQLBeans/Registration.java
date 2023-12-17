package com.example.SQLBeans;

import com.example.Entities.Restaurant;
import jakarta.ejb.Stateless;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpSession;
import lombok.Data;

import java.util.List;

@Stateless
@Named
@Data
public class Registration {

    @Inject
    private HttpSession httpSession;
    public Restaurant restaurant = new Restaurant();
    String persistenceUnitName = "MyAppPersistenceUnit";
    private boolean isSigned = false;
    public String register(){
        try {
            EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
            if (entityManager != null) {
                try {
                    entityManager.getTransaction().begin();
                    entityManager.persist(restaurant);
                    entityManager.getTransaction().commit();
                } finally {
                    entityManager.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return "signin.xhtml?faces-redirect=true";
    }

    public String login(){

        try  {
            EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
            if (entityManager != null) {
                entityManager.getTransaction().begin();
                Query query = entityManager.createQuery("SELECT r FROM Restaurant r WHERE r.name = :name and  r.password = :password");
                query.setParameter("name", restaurant.getName());
                query.setParameter("password",restaurant.getPassword());
                List<?> resultList = query.getResultList();
                entityManager.getTransaction().commit();
                if (!resultList.isEmpty()) {
                    Restaurant r = (Restaurant) resultList.get(0);
                    httpSession.setAttribute("restaurantData", r);
                    isSigned = true;
                    return "index.xhtml?faces-redirect=true";
                }else {
                    FacesContext context = FacesContext.getCurrentInstance();
                    UIComponent passwordComponent = context.getViewRoot().findComponent("loginForm:password");
                    FacesMessage message = new FacesMessage("Incorrect password.");
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);
                    context.addMessage(passwordComponent.getClientId(), message);
                    return "signin.xhtml";
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidatorException(new FacesMessage("Error during validation."));
        }

        return "signin.xhtml";
    }
    public String logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            session.removeAttribute("restaurantData");
            isSigned = false;
        }
        return "restaurents.xhtml?faces-redirect=true";
    }

    public Restaurant getRestaurentInfo(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session1 = (HttpSession) facesContext.getExternalContext().getSession(false);
        Restaurant restaurant = (Restaurant) session1.getAttribute("restaurantData");
        return restaurant;
    }

    public void updateRestaurant(String name, String address, String number) {
        Restaurant restaurant = getRestaurentInfo();
        try {
            EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
            if (entityManager != null) {
                try {
                    entityManager.getTransaction().begin();

                    // Find the existing restaurant entity
                    restaurant = entityManager.find(Restaurant.class, restaurant.getId());

                    // Update the properties
                    restaurant.setName(name);
                    restaurant.setAddress(address);
                    restaurant.setNumber(number);

                    entityManager.getTransaction().commit();

                    httpSession.setAttribute("restaurantData", restaurant);
                } finally {
                    entityManager.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateRestaurantListener(ActionEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String name = (String) facesContext.getExternalContext().getRequestParameterMap().get("formId:name");
        String address = (String) facesContext.getExternalContext().getRequestParameterMap().get("formId:address");
        String number = (String) facesContext.getExternalContext().getRequestParameterMap().get("formId:number");

        updateRestaurant(name, address, number);
    }


    public String redirectToAddPage(){
        return "addPage";
    }
    public String redirectToProfilePage(){
        return "profilePage";
    }
    public String redirectToProfil(){ return  "profil.xhtml"; }
    public String redirectToIndex(){ return  "index.xhtml"; }
}

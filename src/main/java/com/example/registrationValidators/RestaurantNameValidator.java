package com.example.registrationValidators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;


import java.util.List;

@FacesValidator("restaurantNameValidator")
public class RestaurantNameValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String name = (String) value;
        String persistenceUnitName = "MyAppPersistenceUnit";
        if (value == null || name.trim().isEmpty()) {
            FacesMessage message = new FacesMessage("Restaurant Name is required.");
            throw new ValidatorException(message);
        }

        try  {
            EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
            if (entityManager != null) {
                entityManager.getTransaction().begin();
                Query query = entityManager.createQuery("SELECT r FROM Restaurant r WHERE r.name = :name");
                query.setParameter("name", name);
                List<?> resultList = query.getResultList();
                if (!resultList.isEmpty()) {
                    FacesMessage message = new FacesMessage("Restaurant name already exists.");
                    throw new ValidatorException(message);
                }
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidatorException(new FacesMessage("Restaurant name already exists."));
        }
    }
}

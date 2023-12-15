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

@FacesValidator("restaurantLoginNameValidator")
public class RestaurantLoginNameValidator implements Validator<String> {
    String persistenceUnitName = "MyAppPersistenceUnit";

    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        if (value == null || value.trim().isEmpty()) {
            FacesMessage message = new FacesMessage("Restaurant Name is required.");
            throw new ValidatorException(message);
        }

        try  {
            EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
            if (entityManager != null) {
                entityManager.getTransaction().begin();
                Query query = entityManager.createQuery("SELECT r FROM Restaurant r WHERE r.name = :name");
                query.setParameter("name", value);
                List<?> resultList = query.getResultList();
                if (resultList.isEmpty()) {
                    FacesMessage message = new FacesMessage("Restaurant name does not exists.");
                    throw new ValidatorException(message);
                }
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidatorException(new FacesMessage("Restaurant name does not exists."));
        }
    }
}

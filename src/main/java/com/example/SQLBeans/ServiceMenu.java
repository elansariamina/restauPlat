package com.example.SQLBeans;

import com.example.Entities.Line;
import com.example.Entities.Menu;
import com.example.Entities.Restaurant;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import lombok.Data;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.HashMap;

@Stateless
@Named
@Data
public class ServiceMenu {
    String persistenceUnitName = "MyAppPersistenceUnit";
    @Inject
    private HttpSession httpSession;
    private String inputText = "";
    private String rowsValues = "";

    public String createMenuWithLines() {
        EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
        if (entityManager != null) {
            try {
                entityManager.getTransaction().begin();

                Menu menu = new Menu();
                menu.setName(inputText);
                Restaurant restaurant = (Restaurant) httpSession.getAttribute("restaurantData");
                menu.setRestaurant(restaurant);

                HashMap<String, String> prop = new HashMap<>();
                prop.put("1", rowsValues);

                Line line = new Line();
//                line.setMenu(menu);
                line.setPropertyKey("1");
                line.setPropertyValue(rowsValues);

                entityManager.persist(menu);
                entityManager.persist(line);

                entityManager.getTransaction().commit();
                System.out.println("Menu and Line persisted successfully.");
            } catch (Exception e) {
                System.out.println("Error during persistence: " + e.getMessage());
                e.printStackTrace();
                entityManager.getTransaction().rollback();
            } finally {
                entityManager.close();
            }
        }

        return "showPage";
    }
}

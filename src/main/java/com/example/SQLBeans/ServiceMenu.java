package com.example.SQLBeans;

import com.example.Entities.Menu;
import com.example.Entities.Restaurant;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import lombok.Data;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.List;

@Stateless
@Named
@Data
public class ServiceMenu {
    String persistenceUnitName = "MyAppPersistenceUnit";

    @Inject
    private HttpSession httpSession;
    private String inputText = "";
    private String value1 = "";
    private String value2 = "";
    private String value3 = "";
    private String value4 = "";
    private String value5 = "";


    public String createMenu(){
        try {
            jakarta.persistence.EntityManager entityManager = jakarta.persistence.Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
            if (entityManager != null) {
                try {
                    entityManager.getTransaction().begin();
                    Restaurant restaurant = (Restaurant) httpSession.getAttribute("restaurantData");
                    Menu menu = new Menu();
                    menu.setName(inputText);
                    menu.setRestaurant(restaurant);

                    String values = value1 + "/" + value2 + "/" + value3 + "/" + value4 + "/"+value5;
                    menu.setItems(values);

                    entityManager.persist(menu);
                    entityManager.getTransaction().commit();
                } finally {
                    entityManager.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return "showPage";}
}
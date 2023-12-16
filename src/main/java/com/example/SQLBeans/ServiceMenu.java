package com.example.SQLBeans;

import com.example.Entities.Menu;
import com.example.Entities.Restaurant;
import jakarta.ejb.Stateless;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.html.HtmlInputText;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import lombok.Data;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.ArrayList;
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
    private List<List<String>> inputValues = new ArrayList<>();

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

                    StringBuilder values = new StringBuilder();
                    for (List<String> rowValues : inputValues) {
                        values.append(String.join("/", rowValues)).append("/");
                    }

                        menu.setItems(values.toString());

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
        return "showPage";
    }

    public void addRow() {
        List<String> newRowValues = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            newRowValues.add("");
        }
        inputValues.add(newRowValues);
    }
}
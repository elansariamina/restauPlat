package com.example.SQLBeans;

import com.example.Entities.Menu;
import com.example.Entities.Restaurant;
import jakarta.ejb.Stateless;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.html.HtmlInputText;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpSession;
import lombok.Data;

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
                    int count = 0;
                    for (List<String> rowValues : inputValues) {
                        values.append(String.join("/", rowValues)).append(",");
                        count++;
                        if (count % 6 == 0) {
                            values.append(",");
                        }
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
        return "showResult";
    }

    public void addRow() {
        List<String> newRowValues = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            newRowValues.add("");
        }
        inputValues.add(newRowValues);
    }


    List<?> menuListForOneRestaurent = new ArrayList<>();
    public List<?> getMenuListForOneRestaurent() {
        EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
        Restaurant restaurant = (Restaurant) httpSession.getAttribute("restaurantData");

        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT m FROM Menu m where m.restaurant = :restaurant");
        query.setParameter("restaurant", restaurant);
        menuListForOneRestaurent = query.getResultList();
        entityManager.getTransaction().commit();

        return menuListForOneRestaurent;
    }

    List<?> allMenus = new ArrayList<>();
    public List<?> getAllMenusList() {
        EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
        Restaurant restaurant = (Restaurant) httpSession.getAttribute("restaurantData");

        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT m FROM Menu m");
        allMenus = query.getResultList();
        entityManager.getTransaction().commit();

        return allMenus;
    }


    private int menuId;
    private Menu selectedMenu;


    public void loadSelectedMenu() {
        if (menuId > 0) {
            EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();

            try {
                entityManager.getTransaction().begin();
                selectedMenu = entityManager.find(Menu.class, menuId);
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                entityManager.close();
            }
        }
    }

    public String deleteMenu(String menuName) {
        EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Restaurant restaurant = (Restaurant) httpSession.getAttribute("restaurantData");
            Query query = entityManager.createQuery("DELETE FROM Menu m WHERE m.restaurant = :restaurant AND m.name = :menuName");
            query.setParameter("restaurant", restaurant);
            query.setParameter("menuName", menuName);
            query.executeUpdate();

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return "index.xhtml";
    }

}
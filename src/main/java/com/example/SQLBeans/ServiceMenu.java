package com.example.SQLBeans;

import com.example.Entities.Line;
import com.example.Entities.Menu;
import com.example.Entities.Restaurant;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import lombok.Data;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Stateless
@Named
@Data
public class ServiceMenu {
    String persistenceUnitName = "MyAppPersistenceUnit";
    private HttpSession httpSession;
    private String inputText = "";
    private List<List<String>> inputValues = new ArrayList<>();


    public String createMenuWithLines() {
            try {
                EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
                Menu menu = new Menu();
                menu.setName(inputText);
                Restaurant restaurant = (Restaurant) httpSession.getAttribute("restaurantData");
                menu.setRestaurant(restaurant);
                entityManager.persist(menu);

                for (int i = 0; i < inputValues.size(); i++) {
                    List properties = inputValues.get(i);
                    HashMap<String,String> prop = new HashMap<>();
                        for(int j=0 ; j<properties.size();j++){
                    if (properties.get(j)!= null || properties.get(j)!= "") {
                        prop.put(String.valueOf(j),String.valueOf(properties.get(j)));
                    }
                    }
                    Line line = new Line();
                    line.setMenu(menu);
                    line.setProperties(prop);
                    entityManager.persist(line);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

                return "showPage";
            }
}

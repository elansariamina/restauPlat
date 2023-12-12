package com.example.SQLBeans;

import jakarta.annotation.ManagedBean;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import lombok.Data;

import java.util.List;

@Stateless
@Named
@Data
public class TestConnDb {

    public String isConnected = "click conn button";
    public void connection() {

        String persistenceUnitName = "MyAppPersistenceUnit";
        try {
            // Create an EntityManager using the persistence unit name
            EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();

            // Check EntityManager creation
            if (entityManager != null) {
                try {
                    // Begin a transaction
                    entityManager.getTransaction().begin();

                    // Perform a simple query to fetch some data
                    Query query = entityManager.createQuery("SELECT e FROM Restaurant e");
                    List<?> resultList = query.getResultList();

                    // Check if the query executed successfully
                    if (!resultList.isEmpty()) {
                        isConnected = "connected";
                    } else {
                        isConnected = "not connected (no results)";
                    }

                    // Commit the transaction
                    entityManager.getTransaction().commit();
                } finally {
                    // Close the EntityManager
                    entityManager.close();
                }
            } else {
                isConnected = "not connected (EntityManager creation failed)";
            }
        } catch (Exception e) {
            isConnected = "not connected (exception)" + e.getMessage();
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


}

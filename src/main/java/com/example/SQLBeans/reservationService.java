package com.example.SQLBeans;

import com.example.Entities.Reservation;
import com.example.Entities.Restaurant;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpSession;
import lombok.Data;

import java.util.List;

@Stateless
@Named
@Data
public class reservationService {
    String persistenceUnitName = "MyAppPersistenceUnit";
    public Reservation reservation = new Reservation();

    @Inject
    private HttpSession httpSession;
    private List<Reservation> reservations;

    public void insertReservation() {

            EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();

            entityManager.getTransaction().begin();

            if(reservation.getNumTables() <= numOfAvailableTables(reservation.getRestaurantId())){
                entityManager.persist(reservation);
                modifyNumOfAvailableTables(reservation.getRestaurantId(),reservation.getNumTables());
            }

            entityManager.getTransaction().commit();

    }



    public String loadReservations() {
        try {
            EntityManager entityManager = Persistence.createEntityManagerFactory("MyAppPersistenceUnit").createEntityManager();
            entityManager.getTransaction().begin();

            // Get the connected restaurant from the session
            Restaurant connectedRestaurant = (Restaurant) httpSession.getAttribute("restaurantData");

            // Fetch reservations for the connected restaurant
            Query query = entityManager.createQuery("SELECT r FROM Reservation r WHERE r.restaurantId = :restaurantId");
            query.setParameter("restaurantId", connectedRestaurant.getId());
            reservations = query.getResultList();

            entityManager.getTransaction().commit();
            entityManager.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  "reservationsList.xhtml";
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public String deleteReservation(int reservationId, Integer reservationNumTables, int restaurentId) {
        EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Reservation reservation = entityManager.find(Reservation.class, reservationId);

            if (reservation != null) {
                entityManager.remove(reservation);
                modifyNumOfAvailableTables(restaurentId, reservationNumTables * (-1));
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        return "reservationsList.xhtml";
    }

    // helper methode to get the numbre of availabe tables in a restaurent
    public Integer numOfAvailableTables(int restaurentId){
        EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();

        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT r.disponibleTables FROM Restaurant r WHERE r.id = :restaurantId");
            query.setParameter("restaurantId", restaurentId);

            return (Integer) query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }

    // helper method to modify the number of available tables
    public void modifyNumOfAvailableTables(int restaurantId, int numberToSubtract) {
        EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Restaurant restaurant = entityManager.find(Restaurant.class, restaurantId);

            if (restaurant != null) {
                int currentAvailableTables = restaurant.getDisponibleTables();
                int newAvailableTables = Math.max(0, currentAvailableTables - numberToSubtract);
                restaurant.setDisponibleTables(newAvailableTables);

                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

}

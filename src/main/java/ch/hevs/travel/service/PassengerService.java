package ch.hevs.travel.service;

import ch.hevs.travel.entity.Passenger;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

/**
 * Service bean that manages Passenger entities: queries, registration and updates.
 */
@Named
@RequestScoped
public class PassengerService {

    @PersistenceContext(unitName = "travelPU")
    private EntityManager em;

    public List<Passenger> findAllPassengers() {
        return em.createQuery(
            "SELECT p FROM Passenger p ORDER BY p.lastname",
            Passenger.class
        ).getResultList();
    }

    /**
     * Find a passenger by primary key.
     *
     * @param id passenger id
     * @return Passenger or null if not found
     */
    public Passenger findById(Long id) {
        return em.find(Passenger.class, id);
    }

    /**
     * JPQL login query used by SessionBean to authenticate the passenger.
     * Note: this implementation compares the raw password to the stored passwordHash.
     * In a real application, passwords must be hashed and salted properly.
     *
     * @param email passenger email
     * @param password provided password (raw in this demo)
     * @return matching Passenger or null if authentication fails
     */
    public Passenger findByEmailAndPassword(String email, String password) {
        try {
            return em.createQuery(
                "SELECT p FROM Passenger p WHERE p.email = :email AND p.passwordHash = :pwd",
                Passenger.class
            ).setParameter("email", email)
             .setParameter("pwd", password)
             .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Check whether a passenger with the given email already exists.
     *
     * @param email email to check
     * @return true if email is already registered
     */
    public boolean emailExists(String email) {
        Long count = em.createQuery(
            "SELECT COUNT(p) FROM Passenger p WHERE p.email = :email",
            Long.class
        ).setParameter("email", email).getSingleResult();
        return count > 0;
    }

    @Transactional
    /**
     * Register a new passenger (persist entity).
     *
     * @param passenger new passenger to persist
     */
    public void register(Passenger passenger) {
        em.persist(passenger);
    }

    @Transactional
    /**
     * Update an existing passenger (merge entity state).
     *
     * @param passenger passenger with updated fields
     */
    public void update(Passenger passenger) {
        em.merge(passenger);
    }
}
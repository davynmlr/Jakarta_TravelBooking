package ch.hevs.travel.service;

import ch.hevs.travel.entity.Passenger;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

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

    public Passenger findById(Long id) {
        return em.find(Passenger.class, id);
    }

    /**
     * JPQL login query
     * Used by SessionBean to authenticate the passenger
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

    public boolean emailExists(String email) {
        Long count = em.createQuery(
            "SELECT COUNT(p) FROM Passenger p WHERE p.email = :email",
            Long.class
        ).setParameter("email", email).getSingleResult();
        return count > 0;
    }

    @Transactional
    public void register(Passenger passenger) {
        em.persist(passenger);
    }

    @Transactional
    public void update(Passenger passenger) {
        em.merge(passenger);
    }
}
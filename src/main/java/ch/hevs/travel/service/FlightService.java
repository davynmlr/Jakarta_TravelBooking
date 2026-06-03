package ch.hevs.travel.service;

import ch.hevs.travel.entity.Destination;
import ch.hevs.travel.entity.Flight;
import ch.hevs.travel.entity.Passenger;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Named
@RequestScoped
public class FlightService {

    @PersistenceContext(unitName = "travelPU")
    private EntityManager em;

    // ── Flights ────────────────────────────────────────────────────

    public List<Flight> findAllFlights() {
        return em.createQuery(
            "SELECT f FROM Flight f JOIN FETCH f.destination ORDER BY f.departureTime",
            Flight.class
        ).getResultList();
    }

    public List<Flight> findFlightsByDestination(Long destinationId) {
        return em.createQuery(
            "SELECT f FROM Flight f WHERE f.destination.id = :destId ORDER BY f.price",
            Flight.class
        ).setParameter("destId", destinationId).getResultList();
    }

    @Transactional
    public void saveFlight(Flight flight) {
        if (flight.getId() == null) {
            em.persist(flight);
        } else {
            em.merge(flight);
        }
    }

    // ── Destinations ───────────────────────────────────────────────

    public List<Destination> findAllDestinations() {
        return em.createQuery(
            "SELECT d FROM Destination d ORDER BY d.city",
            Destination.class
        ).getResultList();
    }

    public List<Flight> findFlightsByPassengerId(Long passengerId) {
        return em.createQuery(
            "SELECT f FROM Flight f JOIN f.passengers p WHERE p.id = :passengerId ORDER BY f.departureTime",
            Flight.class
        ).setParameter("passengerId", passengerId).getResultList();
    }

    @Transactional
    public void saveDestination(Destination destination) {
        if (destination.getId() == null) {
            em.persist(destination);
        } else {
            em.merge(destination);
        }
    }

    // ── Booking ────────────────────────────────────────────────────

    @Transactional
    public String bookFlight(Long flightId, Long passengerId) {
        Flight flight = em.find(Flight.class, flightId);
        Passenger passenger = em.find(Passenger.class, passengerId);

        if (flight == null || passenger == null) return "error";
        if (!flight.hasAvailableSeats()) return "full";
        if (flight.isBookedBy(passenger)) return "already_booked";

        flight.getPassengers().add(passenger);
        passenger.getFlights().add(flight);
        em.merge(flight);
        em.merge(passenger);
        return "success";
    }

    @Transactional
    public String cancelBooking(Long flightId, Long passengerId) {
        Flight flight = em.find(Flight.class, flightId);
        Passenger passenger = em.find(Passenger.class, passengerId);

        if (flight == null || passenger == null) return "error";

        flight.getPassengers().removeIf(p -> p.getId().equals(passengerId));
        passenger.getFlights().removeIf(f -> f.getId().equals(flightId));
        em.merge(flight);
        em.merge(passenger);
        return "success";
    }
}
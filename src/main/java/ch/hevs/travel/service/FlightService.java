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

/**
 * Service bean that encapsulates flight and destination related operations.
 * Provides methods to query, persist and manage bookings.
 */
@Named
@RequestScoped
public class FlightService {

    @PersistenceContext(unitName = "travelPU")
    private EntityManager em;

    // ── Flights ────────────────────────────────────────────────────

    /**
     * Retrieve all flights with their destinations fetched, ordered by departure time.
     *
     * @return list of flights
     */
    public List<Flight> findAllFlights() {
        return em.createQuery(
            "SELECT f FROM Flight f JOIN FETCH f.destination ORDER BY f.departureTime",
            Flight.class
        ).getResultList();
    }

    /**
     * Find flights for a specific destination
     *
     * @param destinationId id of the destination
     * @return list of flights for the destination
     */
    public List<Flight> findFlightsByDestination(Long destinationId) {
        return em.createQuery(
            "SELECT f FROM Flight f WHERE f.destination.id = :destId ORDER BY f.price",
            Flight.class
        ).setParameter("destId", destinationId).getResultList();
    }

    @Transactional
    /**
     * Persist or merge a Flight entity.
     *
     * @param flight flight to save
     */
    public void saveFlight(Flight flight) {
        if (flight.getId() == null) {
            em.persist(flight);
        } else {
            em.merge(flight);
        }
    }

    // ── Destinations ───────────────────────────────────────────────

    /**
     * List all destinations ordered by city name.
     *
     * @return list of destinations
     */
    public List<Destination> findAllDestinations() {
        return em.createQuery(
            "SELECT d FROM Destination d ORDER BY d.city",
            Destination.class
        ).getResultList();
    }

    /**
     * Find flights booked by a specific passenger.
     *
     * @param passengerId passenger id
     * @return list of flights booked by passenger
     */
    public List<Flight> findFlightsByPassengerId(Long passengerId) {
        return em.createQuery(
            "SELECT f FROM Flight f JOIN f.passengers p WHERE p.id = :passengerId ORDER BY f.departureTime",
            Flight.class
        ).setParameter("passengerId", passengerId).getResultList();
    }

    @Transactional
    /**
     * Persist or merge a Destination entity.
     *
     * @param destination destination to save
     */
    public void saveDestination(Destination destination) {
        if (destination.getId() == null) {
            em.persist(destination);
        } else {
            em.merge(destination);
        }
    }

    // ── Booking ────────────────────────────────────────────────────

    @Transactional
    /**
     * Book a flight for a passenger. Performs basic checks (exists, capacity, already booked)
     * and updates both sides of the many-to-many relationship.
     *
     * @param flightId id of the flight
     * @param passengerId id of the passenger
     * @return "success", "full", "already_booked" or "error"
     */
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
    /**
     * Cancel a passenger's booking on a flight and update both entities.
     *
     * @param flightId id of the flight
     * @param passengerId id of the passenger
     * @return "success" or "error"
     */
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
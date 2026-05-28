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

    public List<Flight> findEconomyFlights() {
        return em.createQuery(
            "SELECT f FROM EconomyFlight f JOIN FETCH f.destination",
            Flight.class
        ).getResultList();
    }

    public List<Flight> findBusinessFlights() {
        return em.createQuery(
            "SELECT f FROM BusinessFlight f JOIN FETCH f.destination",
            Flight.class
        ).getResultList();
    }

    public Flight findFlightById(Long id) {
        return em.find(Flight.class, id);
    }

    @Transactional
    public void saveFlight(Flight flight) {
        if (flight.getId() == null) {
            em.persist(flight);
        } else {
            em.merge(flight);
        }
    }

    @Transactional
    public void deleteFlight(Long id) {
        Flight f = em.find(Flight.class, id);
        if (f != null) em.remove(f);
    }

    // ── Destinations ───────────────────────────────────────────────

    public List<Destination> findAllDestinations() {
        return em.createQuery(
            "SELECT d FROM Destination d ORDER BY d.city",
            Destination.class
        ).getResultList();
    }

    public Destination findDestinationById(Long id) {
        return em.find(Destination.class, id);
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

    /**
     * @Transactional justified here because:
     * 1. Two DB writes must happen together (link passenger + flight)
     * 2. Capacity check + booking must be atomic
     * 3. If anything fails, everything rolls back
     */
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
        return "success";
    }

    /**
     * @Transactional justified here because:
     * Both sides of the ManyToMany must be cleaned up atomically
     */
    @Transactional
    public String cancelBooking(Long flightId, Long passengerId) {
        Flight flight = em.find(Flight.class, flightId);
        Passenger passenger = em.find(Passenger.class, passengerId);

        if (flight == null || passenger == null) return "error";

        flight.getPassengers().removeIf(p -> p.getId().equals(passengerId));
        passenger.getFlights().removeIf(f -> f.getId().equals(flightId));
        em.merge(flight);
        return "success";
    }
}
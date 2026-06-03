package ch.hevs.travel.web;

import ch.hevs.travel.entity.Destination;
import ch.hevs.travel.entity.Flight;
import ch.hevs.travel.service.FlightService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class FlightBean {

    @Inject
    private FlightService flightService;

    @Inject
    private SessionBean sessionBean;

    private List<Flight> flights;
    private List<Destination> destinations;
    private Long selectedDestinationId;
    private String selectedCabinClass = "ALL";
    private String bookingMessage;

    @PostConstruct
    public void init() {
        destinations = flightService.findAllDestinations();
        flights = flightService.findAllFlights();
    }

    // ── Actions ────────────────────────────────────────────────────

    public String bookFlight(Long flightId) {
        if (!sessionBean.isLoggedIn()) {
            return "login?faces-redirect=true";
        }
        Long passengerId = sessionBean.getCurrentPassenger().getId();
        String result = flightService.bookFlight(flightId, passengerId);

        switch (result) {
            case "success":        bookingMessage = "Flight booked successfully!"; break;
            case "full":           bookingMessage = "Sorry, this flight is full."; break;
            case "already_booked": bookingMessage = "You already booked this flight."; break;
            default:               bookingMessage = "An error occurred.";
        }
        flights = flightService.findAllFlights();
        return null;
    }

    public String cancelFlight(Long flightId) {
        if (!sessionBean.isLoggedIn()) {
            return "login?faces-redirect=true";
        }
        Long passengerId = sessionBean.getCurrentPassenger().getId();
        String result = flightService.cancelBooking(flightId, passengerId);
        bookingMessage = result.equals("success")
            ? "Booking cancelled."
            : "Could not cancel booking.";
        flights = flightService.findAllFlights();
        return null;
    }

    public void filterFlights() {
        List<Flight> all;

        // First filter by destination
        if (selectedDestinationId == null || selectedDestinationId == 0L) {
            all = flightService.findAllFlights();
        } else {
            all = flightService.findFlightsByDestination(selectedDestinationId);
        }

        // Then filter by cabin class
        if (selectedCabinClass == null || selectedCabinClass.equals("ALL")) {
            flights = all;
        } else {
            flights = all.stream()
                .filter(f -> f.getCabinClass().equals(selectedCabinClass))
                .collect(java.util.stream.Collectors.toList());
        }
    }

    public boolean isBookedByCurrentUser(Flight flight) {
        if (!sessionBean.isLoggedIn()) return false;
        return flight.isBookedBy(sessionBean.getCurrentPassenger());
    }

    public List<Flight> getCurrentPassengerFlights() {
        if (!sessionBean.isLoggedIn()) {
            return java.util.Collections.emptyList();
        }
        return flightService.findFlightsByPassengerId(sessionBean.getCurrentPassenger().getId());
    }

    // ── Getters / Setters ──────────────────────────────────────────
    public List<Flight> getFlights()              { return flights; }
    public List<Destination> getDestinations()    { return destinations; }
    public String getBookingMessage()             { return bookingMessage; }

    public Long getSelectedDestinationId()        { return selectedDestinationId; }
    public void setSelectedDestinationId(Long id) { this.selectedDestinationId = id; }
    public String getSelectedCabinClass()          { return selectedCabinClass; }
    public void setSelectedCabinClass(String c)    { this.selectedCabinClass = c; }
}
package ch.hevs.travel.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Abstract base entity representing a flight.
 * Subclasses represent specific cabin classes (e.g. Economy, Business).
 * This entity is stored in a single table inheritance strategy with a discriminator column.
 */
@Entity
@Table(name = "Flight")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String flightNumber;
    private String airline;

    @Temporal(TemporalType.TIMESTAMP)
    private Date departureTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date arrivalTime;

    private double price;
    private int capacity;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_id")
    private Destination destination;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
        name = "flight_passenger",
        joinColumns = @JoinColumn(name = "flight_id"),
        inverseJoinColumns = @JoinColumn(name = "passenger_id")
    )
    private List<Passenger> passengers = new ArrayList<>();

    public Flight() {}

    public Flight(String flightNumber, String airline, Date departureTime,
                  Date arrivalTime, double price, int capacity, Destination destination) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.capacity = capacity;
        this.destination = destination;
    }

    /**
     * Return the cabin class name for this flight (e.g. "Economy", "Business").
     * Each concrete subclass must provide its own value.
     *
     * @return the cabin class name
     */
    public abstract String getCabinClass();

    /**
     * Check whether the flight still has available seats.
     *
     * @return true if the number of booked passengers is less than capacity
     */
    public boolean hasAvailableSeats() {
        return passengers.size() < capacity;
    }

    /**
     * Check whether the given passenger has booked this flight.
     *
     * @param p passenger to check
     * @return true if the passenger is present in the passengers list
     */
    public boolean isBookedBy(Passenger p) {
        return passengers.contains(p);
    }

    public Long getId()                         { return id; }
    public void setId(Long id)                  { this.id = id; }

    public String getFlightNumber()             { return flightNumber; }
    public void setFlightNumber(String fn)      { this.flightNumber = fn; }

    public String getAirline()                  { return airline; }
    public void setAirline(String a)            { this.airline = a; }

    public Date getDepartureTime()              { return departureTime; }
    public void setDepartureTime(Date d)        { this.departureTime = d; }

    public Date getArrivalTime()                { return arrivalTime; }
    public void setArrivalTime(Date d)          { this.arrivalTime = d; }

    public double getPrice()                    { return price; }
    public void setPrice(double p)              { this.price = p; }

    public int getCapacity()                    { return capacity; }
    public void setCapacity(int c)              { this.capacity = c; }

    public Destination getDestination()         { return destination; }
    public void setDestination(Destination d)   { this.destination = d; }

    public List<Passenger> getPassengers()      { return passengers; }
    public void setPassengers(List<Passenger> p){ this.passengers = p; }

    public int getBookedSeats()                 { return passengers.size(); }

    @Override
    public String toString() {
        return flightNumber + " → " + (destination != null ? destination.getCity() : "?");
    }
}
package ch.hevs.travel.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a travel destination (city/airport) and the associated flights.
 * Stored as a JPA entity with a one-to-many relationship to Flight.
 */
@Entity
@Table(name = "Destination")
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String country;
    private String airport;
    private String airportCode;
    private String timezone;

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Flight> flights = new ArrayList<>();

    public Destination() {}

    public Destination(String city, String country, String airport,
                       String airportCode, String timezone) {
        this.city = city;
        this.country = country;
        this.airport = airport;
        this.airportCode = airportCode;
        this.timezone = timezone;
    }

    public Long getId()                    { return id; }
    public void setId(Long id)             { this.id = id; }

    public String getCity()                { return city; }
    public void setCity(String city)       { this.city = city; }

    public String getCountry()             { return country; }
    public void setCountry(String c)       { this.country = c; }

    public String getAirport()             { return airport; }
    public void setAirport(String a)       { this.airport = a; }

    public String getAirportCode()         { return airportCode; }
    public void setAirportCode(String c)   { this.airportCode = c; }

    public String getTimezone()            { return timezone; }
    public void setTimezone(String t)      { this.timezone = t; }

    /**
     * Get flights associated with this destination.
     * Be careful: the list is fetched eagerly in this model.
     */
    public List<Flight> getFlights()       { return flights; }
    public void setFlights(List<Flight> f) { this.flights = f; }

    @Override
    public String toString() {
        return city + " (" + airportCode + "), " + country;
    }
}
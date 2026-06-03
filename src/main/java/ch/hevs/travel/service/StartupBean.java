package ch.hevs.travel.service;

import ch.hevs.travel.entity.*;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;

/**
 * Runs once when the app starts and inserts sample data
 */
@Singleton
@Startup
public class StartupBean {

    @PersistenceContext(unitName = "travelPU")
    private EntityManager em;

    @PostConstruct
    @Transactional
    public void init() {

        // ── Destinations ───────────────────────────────────────────
        Destination paris   = new Destination("Paris",    "France",  "Charles de Gaulle", "CDG", "Europe/Paris");
        Destination newYork = new Destination("New York", "USA",     "John F. Kennedy",   "JFK", "America/New_York");
        Destination tokyo   = new Destination("Tokyo",    "Japan",   "Narita",            "NRT", "Asia/Tokyo");
        Destination dubai   = new Destination("Dubai",    "UAE",     "Dubai Intl",        "DXB", "Asia/Dubai");
        Destination london  = new Destination("London",   "UK",      "Heathrow",          "LHR", "Europe/London");

        em.persist(paris);
        em.persist(newYork);
        em.persist(tokyo);
        em.persist(dubai);
        em.persist(london);

        // ── Flights ────────────────────────────────────────────────
        EconomyFlight f1 = new EconomyFlight();
        f1.setFlightNumber("LX238");
        f1.setAirline("Swiss Air");
        f1.setDepartureTime(makeDate(2026, 7, 1, 8, 0));
        f1.setArrivalTime(makeDate(2026, 7, 1, 10, 30));
        f1.setPrice(250.0);
        f1.setCapacity(150);
        f1.setDestination(paris);
        f1.setMealIncluded(false);

        BusinessFlight f2 = new BusinessFlight();
        f2.setFlightNumber("LX100");
        f2.setAirline("Swiss Air");
        f2.setDepartureTime(makeDate(2026, 7, 2, 9, 0));
        f2.setArrivalTime(makeDate(2026, 7, 2, 15, 30));
        f2.setPrice(1200.0);
        f2.setCapacity(40);
        f2.setDestination(newYork);
        f2.setLoungeAccess(true);

        EconomyFlight f3 = new EconomyFlight();
        f3.setFlightNumber("EK412");
        f3.setAirline("Emirates");
        f3.setDepartureTime(makeDate(2026, 7, 3, 14, 0));
        f3.setArrivalTime(makeDate(2026, 7, 4, 6, 0));
        f3.setPrice(890.0);
        f3.setCapacity(200);
        f3.setDestination(tokyo);
        f3.setMealIncluded(true);

        BusinessFlight f4 = new BusinessFlight();
        f4.setFlightNumber("EK002");
        f4.setAirline("Emirates");
        f4.setDepartureTime(makeDate(2026, 7, 5, 22, 0));
        f4.setArrivalTime(makeDate(2026, 7, 6, 6, 0));
        f4.setPrice(3500.0);
        f4.setCapacity(30);
        f4.setDestination(dubai);
        f4.setLoungeAccess(true);

        EconomyFlight f5 = new EconomyFlight();
        f5.setFlightNumber("BA726");
        f5.setAirline("British Airways");
        f5.setDepartureTime(makeDate(2026, 7, 10, 7, 0));
        f5.setArrivalTime(makeDate(2026, 7, 10, 8, 30));
        f5.setPrice(180.0);
        f5.setCapacity(180);
        f5.setDestination(london);
        f5.setMealIncluded(false);

        em.persist(f1);
        em.persist(f2);
        em.persist(f3);
        em.persist(f4);
        em.persist(f5);

        // ── Passengers ─────────────────────────────────────────────
        Passenger alice = new Passenger();
        alice.setFirstname("Alice");
        alice.setLastname("Martin");
        alice.setEmail("alice@example.com");
        alice.setPasswordHash("password123");
        alice.setPassport("CH123456");
        alice.setNationality("Swiss");

        Passenger bob = new Passenger();
        bob.setFirstname("Bob");
        bob.setLastname("Smith");
        bob.setEmail("bob@example.com");
        bob.setPasswordHash("password123");
        bob.setPassport("US789012");
        bob.setNationality("American");

        Passenger chidi = new Passenger();
        chidi.setFirstname("Chidi");
        chidi.setLastname("Okafor");
        chidi.setEmail("chidi@example.com");
        chidi.setPasswordHash("password123");
        chidi.setPassport("NG345678");
        chidi.setNationality("Nigerian");

        em.persist(alice);
        em.persist(bob);
        em.persist(chidi);
    }

    private Date makeDate(int year, int month, int day, int hour, int min) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day, hour, min, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
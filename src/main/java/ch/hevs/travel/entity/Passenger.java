package ch.hevs.travel.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Passenger")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;

    @Column(unique = true, nullable = false)
    private String email;

    private String passport;
    private String nationality;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    private String passwordHash;

    @ManyToMany(mappedBy = "passengers", fetch = FetchType.EAGER)
    private List<Flight> flights = new ArrayList<>();

    public Passenger() {}

    public Passenger(String firstname, String lastname, String email,
                     String passport, String nationality, Date dateOfBirth,
                     String passwordHash) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.passport = passport;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.passwordHash = passwordHash;
    }

    public Long getId()                         { return id; }
    public void setId(Long id)                  { this.id = id; }

    public String getFirstname()                { return firstname; }
    public void setFirstname(String f)          { this.firstname = f; }

    public String getLastname()                 { return lastname; }
    public void setLastname(String l)           { this.lastname = l; }

    public String getEmail()                    { return email; }
    public void setEmail(String e)              { this.email = e; }

    public String getPassport()                 { return passport; }
    public void setPassport(String p)           { this.passport = p; }

    public String getNationality()              { return nationality; }
    public void setNationality(String n)        { this.nationality = n; }

    public Date getDateOfBirth()                { return dateOfBirth; }
    public void setDateOfBirth(Date d)          { this.dateOfBirth = d; }

    public String getPasswordHash()             { return passwordHash; }
    public void setPasswordHash(String p)       { this.passwordHash = p; }

    public List<Flight> getFlights()            { return flights; }
    public void setFlights(List<Flight> f)      { this.flights = f; }

    public String getFullName()                 { return firstname + " " + lastname; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Passenger)) return false;
        return id != null && id.equals(((Passenger) o).id);
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }

    @Override
    public String toString() { return getFullName() + " <" + email + ">"; }
}
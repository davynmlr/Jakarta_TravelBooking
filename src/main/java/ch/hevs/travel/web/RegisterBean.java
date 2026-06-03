package ch.hevs.travel.web;

import ch.hevs.travel.entity.Passenger;
import ch.hevs.travel.service.PassengerService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class RegisterBean {

    @Inject
    private PassengerService passengerService;

    @Inject
    private SessionBean sessionBean;

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String passport;
    private String nationality;
    private String errorMessage;

    public String register() {
        if (passengerService.emailExists(email)) {
            errorMessage = "This email is already registered.";
            return null;
        }

        Passenger p = new Passenger();
        p.setFirstname(firstname);
        p.setLastname(lastname);
        p.setEmail(email);
        p.setPasswordHash(password);
        p.setPassport(passport);
        p.setNationality(nationality);

        passengerService.register(p);

        // Auto-login after registration
        sessionBean.setCurrentPassenger(p);
        return "flights?faces-redirect=true";
    }

    // ── Getters / Setters ──────────────────────────────────────────
    public String getFirstname()         { return firstname; }
    public void setFirstname(String v)   { this.firstname = v; }

    public String getLastname()          { return lastname; }
    public void setLastname(String v)    { this.lastname = v; }

    public String getEmail()             { return email; }
    public void setEmail(String v)       { this.email = v; }

    public String getPassword()          { return password; }
    public void setPassword(String v)    { this.password = v; }

    public String getPassport()          { return passport; }
    public void setPassport(String v)    { this.passport = v; }

    public String getNationality()       { return nationality; }
    public void setNationality(String v) { this.nationality = v; }

    public String getErrorMessage()      { return errorMessage; }
}
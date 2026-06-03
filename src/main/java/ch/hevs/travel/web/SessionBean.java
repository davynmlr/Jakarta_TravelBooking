package ch.hevs.travel.web;

import ch.hevs.travel.entity.Passenger;
import ch.hevs.travel.service.PassengerService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serial;
import java.io.Serializable;

/**
 * Session-scoped bean that holds authentication state for the current HTTP session.
 * One instance exists per user session and it keeps the logged-in Passenger across pages.
 * The bean is Serializable so the container can passivate/activate it.
 */
@Named
@SessionScoped
public class SessionBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private PassengerService passengerService;

    private String email;
    private String password;
    private String loginError;
    private Passenger currentPassenger;

    // ── Login / Logout ─────────────────────────────────────────────

    /**
     * Called by the login form
     * Validates credentials and stores the passenger in session
     */
    public String login() {
        Passenger found = passengerService.findByEmailAndPassword(email, password);
        if (found != null) {
            this.currentPassenger = found;
            this.loginError = null;
            this.password = null;
            return "flights?faces-redirect=true";
        } else {
            this.loginError = "Invalid email or password.";
            return null;
        }
    }

    /**
     * Called by the logout button
     * Clears the session and redirects to login
     */
    public String logout() {
        this.currentPassenger = null;
        this.email = null;
        this.password = null;
        this.loginError = null;
        return "login?faces-redirect=true";
    }

    // ── Guard ──────────────────────────────────────────────────────

    public boolean isLoggedIn() {
        return currentPassenger != null;
    }

    /**
     * Call this at the top of protected pages
     * Redirects to login if no passenger is in session
     */
    public String requireLogin() {
        if (currentPassenger == null) {
            return "login?faces-redirect=true";
        }
        return null;
    }

    // ── Getters / Setters ──────────────────────────────────────────
    /**
     * Return the current passenger stored in session. If the passenger has an id,
     * fresh data is loaded from the database to avoid stale detached entities.
     *
     * @return current Passenger or null
     */
    public Passenger getCurrentPassenger() {
        if (currentPassenger == null || currentPassenger.getId() == null) {
            return currentPassenger;
        }
        Passenger fresh = passengerService.findById(currentPassenger.getId());
        if (fresh != null) {
            currentPassenger = fresh;
        }
        return currentPassenger;
    }

    public void setCurrentPassenger(Passenger p) { this.currentPassenger = p; }

    public String getEmail()                     { return email; }
    public void setEmail(String e)               { this.email = e; }

    public String getPassword()                  { return password; }
    public void setPassword(String p)            { this.password = p; }

    public String getLoginError()                { return loginError; }
}
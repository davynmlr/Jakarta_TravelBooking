package ch.hevs.travel.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Concrete Flight representing a Business class cabin.
 * Adds business-specific features such as lounge access.
 */
@Entity
@DiscriminatorValue("BusinessFlight")
public class BusinessFlight extends Flight {

    // Indicates whether lounge access is included for this business flight
    private boolean loungeAccess = true;

    public BusinessFlight() {}

    @Override
    public String getCabinClass() {
        return "Business";
    }

    /**
     * Whether lounge access is available for this booking.
     */
    public boolean isLoungeAccess()        { return loungeAccess; }

    public void setLoungeAccess(boolean l) { this.loungeAccess = l; }
}
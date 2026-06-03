package ch.hevs.travel.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BusinessFlight")
public class BusinessFlight extends Flight {

    private boolean loungeAccess = true;

    public BusinessFlight() {}

    @Override
    public String getCabinClass() {
        return "Business";
    }

    public boolean isLoungeAccess()        { return loungeAccess; }
    public void setLoungeAccess(boolean l) { this.loungeAccess = l; }
}
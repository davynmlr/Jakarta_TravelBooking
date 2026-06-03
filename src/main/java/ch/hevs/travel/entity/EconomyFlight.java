package ch.hevs.travel.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Concrete Flight representing an Economy class cabin.
 * Adds economy-specific features such as meal inclusion.
 */
@Entity
@DiscriminatorValue("EconomyFlight")
public class EconomyFlight extends Flight {

    // Whether a meal is included in the fare
    private boolean mealIncluded = false;

    public EconomyFlight() {}

    @Override
    public String getCabinClass() {
        return "Economy";
    }

    /**
     * Whether a meal is included with this flight booking.
     */
    public boolean isMealIncluded()        { return mealIncluded; }
    public void setMealIncluded(boolean m) { this.mealIncluded = m; }
}
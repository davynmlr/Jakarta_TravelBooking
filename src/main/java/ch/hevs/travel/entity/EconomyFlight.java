package ch.hevs.travel.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("EconomyFlight")
public class EconomyFlight extends Flight {

    private boolean mealIncluded = false;

    public EconomyFlight() {}

    @Override
    public String getCabinClass() {
        return "Economy";
    }

    public boolean isMealIncluded()        { return mealIncluded; }
    public void setMealIncluded(boolean m) { this.mealIncluded = m; }
}
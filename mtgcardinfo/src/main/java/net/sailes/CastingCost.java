package net.sailes;

public class CastingCost {

    private final String text;

    public CastingCost(String castingCost) {
        this.text = castingCost;
    }

    public String text() {
        return this.text;
    }

    public String toString() {
        return text();
    }
}

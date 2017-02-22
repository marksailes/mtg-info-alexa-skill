package net.sailes.alexa.mtg;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CastingCost {

    private final String encodedCastingCost;
    private final String castingCostAsText;

    public CastingCost(String encodedCastingCost, String castingCostAsText) {
        this.encodedCastingCost = encodedCastingCost;
        this.castingCostAsText = castingCostAsText;
    }

    public String text() {
        return this.castingCostAsText;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("encodedCastingCost", encodedCastingCost)
                .append("castingCostAsText", castingCostAsText)
                .toString();
    }
}

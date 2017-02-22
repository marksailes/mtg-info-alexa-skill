package net.sailes.alexa.mtg;

public interface MtgCardInfo {

    CastingCost castingCost(String cardName);

    String description(String cardName);
}

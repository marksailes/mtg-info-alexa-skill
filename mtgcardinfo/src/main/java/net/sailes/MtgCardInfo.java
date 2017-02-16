package net.sailes;

public interface MtgCardInfo {

    CastingCost castingCost(String cardName);

    String description(String cardName);

    String fullInformation(String cardName);
}

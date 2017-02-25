package net.sailes.alexa.mtg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockMtgCardInfo implements MtgCardInfo {

    private static final Logger log = LoggerFactory.getLogger(MockMtgCardInfo.class);

    @Override
    public CastingCost castingCost(String cardName) {
        log.info("Calling mock casting cost");
        return new CastingCost("{U}", "1 blue");
    }

    @Override
    public String description(String cardName) {
        log.info("Calling mock description");
        return "mock description";
    }
}

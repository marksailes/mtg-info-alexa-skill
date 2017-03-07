package net.sailes.alexa.mtg;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import net.sailes.alexa.mtg.deckbrew.DeckbrewCardInfo;

import java.util.HashSet;
import java.util.Set;

public final class MtgCardInfoSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {

    private static final Set<String> supportedApplicationIds = new HashSet<>();

    static {
        supportedApplicationIds.add("amzn1.ask.skill.37b270b0-01d0-473f-afaa-7b843b788c54");
    }

    public MtgCardInfoSpeechletRequestStreamHandler() {
        super(new MtgCardInfoSpeechlet(new DeckbrewCardInfo()), supportedApplicationIds);
    }
}
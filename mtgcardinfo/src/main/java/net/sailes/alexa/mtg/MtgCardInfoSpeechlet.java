package net.sailes.alexa.mtg;

import com.amazon.speech.slu.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;

import static net.sailes.alexa.mtg.Intents.*;

public class MtgCardInfoSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(MtgCardInfoSpeechlet.class);

    public static final String CARDNAME_SLOT_KEY = "cardname";

    private static final String CAST_COST_SENTANCE_FORMAT = "The casting cost for %s is %s";
    private static final String DESCRIPTION_SENTANCE_FORMAT = "The rules text for %s is %s.";

    private final MtgCardInfo mtgCardInfo;

    public MtgCardInfoSpeechlet(MtgCardInfo mtgCardInfo) {
        this.mtgCardInfo = mtgCardInfo;
    }

    public void onSessionStarted(final SessionStartedRequest request, final Session session) throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
    }

    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
        return SimpleResponseFactory.simpleAskResponse("Welcome to Magic Info, you can ask about casting costs and rules text", "Magic Info");
    }

    public SpeechletResponse onIntent(final IntentRequest request, final Session session) throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if (INTENT_MTG_CASTING_COST.equals(intentName)) {
            Slot cardName = request.getIntent().getSlot(CARDNAME_SLOT_KEY);

            if (cardName == null || cardName.getValue() == null) {
                return SimpleResponseFactory.unrecognisedCardResponse();
            }

            return getCastingCostResponse(cardName.getValue());
        } else if (INTENT_MTG_DESCRIPTION.equals(intentName)) {
            Slot cardName = request.getIntent().getSlot(CARDNAME_SLOT_KEY);

            if (cardName == null || cardName.getValue() == null) {
                return SimpleResponseFactory.unrecognisedCardResponse();
            }

            return getDescriptionResponse(cardName.getValue());
        } else if (AMAZON_HELP_INTENT.equals(intentName)) {
            return SimpleResponseFactory.simpleAskResponse("You can ask me about casting costs", "Help");
        } else if (AMAZON_STOP_INTENT.equals(intentName)) {
            return SimpleResponseFactory.simpleTellResponse("Request stopped", "Stop");
        } else if (AMAZON_CANCEL_INTENT.equals(intentName)) {
            return SimpleResponseFactory.simpleTellResponse("Request cancelled", "Cancel");
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    private SpeechletResponse getCastingCostResponse(String cardName) {
        CastingCost castingCost = this.mtgCardInfo.castingCost(cardName);
        log.info("{}", castingCost);

        String text = String.format(CAST_COST_SENTANCE_FORMAT, cardName, castingCost.text());
        return SimpleResponseFactory.simpleTellResponse(text, cardName + " casting cost");
    }

    private SpeechletResponse getDescriptionResponse(String cardName) {
        String description = this.mtgCardInfo.description(cardName);
        String speechText = String.format(DESCRIPTION_SENTANCE_FORMAT, cardName, description);

        return SimpleResponseFactory.simpleTellResponse(speechText, cardName + " description");
    }

    public void onSessionEnded(final SessionEndedRequest request, final Session session) throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
    }
}
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

public class MtgCardInfoSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(MtgCardInfoSpeechlet.class);

    private static final String INTENT_MTG_CASTING_COST = "MtgCastingCost";
    private static final String INTENT_MTG_DESCRIPTION = "MtgDescription";
    private static final String INTENT_MTG_FULL_INFORMATION = "MtgFullInformation";
    private static final String AMAZON_HELP_INTENT = "AMAZON.HelpIntent";
    private static final String AMAZON_STOP_INTENT = "AMAZON.StopIntent";
    private static final String AMAZON_CANCEL_INTENT = "AMAZON.CancelIntent";
    public static final String CARDNAME_SLOT_KEY = "cardname";

    private MtgCardInfo mtgCardInfo = new DeckbrewCardInfo();

    public void onSessionStarted(final SessionStartedRequest request, final Session session) throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
    }

    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
        return SimpleResponseFactory.simpleAskResponse("Welcome to the Alexa Skills Kit, you can say hello", "HelloWorld");
    }

    public SpeechletResponse onIntent(final IntentRequest request, final Session session) throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if (INTENT_MTG_CASTING_COST.equals(intentName)) {
            Slot cardName = request.getIntent().getSlot(CARDNAME_SLOT_KEY);

            if (cardName == null) {
                return SimpleResponseFactory.unrecognisedCardResponse();
            }

            return getCastingCostResponse(cardName.getValue());
        } else if (INTENT_MTG_DESCRIPTION.equals(intentName)) {
            Slot cardName = request.getIntent().getSlot(CARDNAME_SLOT_KEY);

            if (cardName == null) {
                return SimpleResponseFactory.unrecognisedCardResponse();
            }
            return getDescriptionResponse(cardName.getValue());
        } else if (INTENT_MTG_FULL_INFORMATION.equals(intentName)) {
            return getFullInformationResponse();
        } else if (AMAZON_HELP_INTENT.equals(intentName)) {
            return SimpleResponseFactory.simpleAskResponse("You can ask me about casting costs", "Help");
        } else if (AMAZON_STOP_INTENT.equals(intentName)) {
            return SimpleResponseFactory.simpleAskResponse("Request stopped", "Stop");
        } else if (AMAZON_CANCEL_INTENT.equals(intentName)) {
            return SimpleResponseFactory.simpleAskResponse("Request cancelled", "Cancel");
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    private SpeechletResponse getCastingCostResponse(String cardName) {
        CastingCost castingCost = this.mtgCardInfo.castingCost(cardName);

        String speechTextFormat = "The casting cost for %s is %s";
        String text = String.format(speechTextFormat, cardName, castingCost);
        return SimpleResponseFactory.simpleTellResponse(text, "title");
    }

    private SpeechletResponse getDescriptionResponse(String cardName) {
        String description = this.mtgCardInfo.description(cardName);
        String speechTextFormat = "The rules text for %s is %s.";
        String speechText = String.format(speechTextFormat, cardName, description);

        return SimpleResponseFactory.simpleAskResponse(speechText, cardName + " description");
    }

    private SpeechletResponse getFullInformationResponse() {
        String speechText = "3 blue blue. Draw 3 cards.";

        return SimpleResponseFactory.simpleAskResponse(speechText, "Ponder information");
    }

    public void onSessionEnded(final SessionEndedRequest request, final Session session) throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
    }
}
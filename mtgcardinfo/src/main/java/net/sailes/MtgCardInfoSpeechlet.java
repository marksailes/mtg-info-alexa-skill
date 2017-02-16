package net.sailes;

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
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

public class MtgCardInfoSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(MtgCardInfoSpeechlet.class);

    private MtgCardInfo mtgCardInfo = new DeckbrewCardInfo();

    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
    }

    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        return getWelcomeResponse();
    }

    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if ("MtgCastingCost".equals(intentName)) {
            Slot cardname = request.getIntent().getSlot("cardname");

            if (cardname == null) {
                return textResponse("Sorry, I didn't hear the card name, could you repeat it?");
            }

            return getCastingCostResponse(cardname.getValue());
        } else if ("MtgDescription".equals(intentName)) {
            Slot cardname = request.getIntent().getSlot("cardname");

            if (cardname == null) {
                return textResponse("Sorry, I didn't hear the card name, could you repeat it?");
            }
            return getDescriptionResponse(cardname.getValue());
        } else if ("MtgFullInformation".equals(intentName)) {
            return getFullInformationResponse();
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse();
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    private SpeechletResponse getCastingCostResponse(String cardName) {
        CastingCost castingCost = this.mtgCardInfo.castingCost(cardName);

        String speechTextFormat = "the casting cost for %s is %s";
        String text = String.format(speechTextFormat, cardName, castingCost);
        return textResponse(text);
    }

    private SpeechletResponse textResponse(String text) {
        SimpleCard card = new SimpleCard();
        card.setTitle("title");
        card.setContent(text);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(text);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    private SpeechletResponse getDescriptionResponse(String cardname) {
        String description = this.mtgCardInfo.description(cardname);
        String speechTextFormat = "The rules text for %s is %s.";
        String speechText = String.format(speechTextFormat, cardname, description);

        SimpleCard card = new SimpleCard();
        card.setTitle(cardname + " description");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    private SpeechletResponse getFullInformationResponse() {
        String speechText = "3 blue blue. Draw 3 cards.";

        SimpleCard card = new SimpleCard();
        card.setTitle("Ponder information");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
    }

    private SpeechletResponse getWelcomeResponse() {
        String speechText = "Welcome to the Alexa Skills Kit, you can say hello";

        SimpleCard card = new SimpleCard();
        card.setTitle("HelloWorld");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    private SpeechletResponse getHelpResponse() {
        String speechText = "You can say hello to me!";

        SimpleCard card = new SimpleCard();
        card.setTitle("HelloWorld");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
}
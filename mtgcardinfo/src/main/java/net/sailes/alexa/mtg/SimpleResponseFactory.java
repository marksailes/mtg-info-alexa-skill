package net.sailes.alexa.mtg;

import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

public class SimpleResponseFactory {

    public static SpeechletResponse simpleAskResponse(String speechText, String title) {
        SimpleCard card = new SimpleCard();
        card.setTitle(title);
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    public static SpeechletResponse simpleTellResponse(String speechText, String title) {
        SimpleCard card = new SimpleCard();
        card.setTitle(title);
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    public static SpeechletResponse unrecognisedCardResponse() {
        return simpleTellResponse("Sorry, I didn't hear the card name, could you repeat it?", "sorry");
    }
}

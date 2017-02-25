package net.sailes.alexa.mtg;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.sailes.alexa.mtg.MtgCardInfoSpeechlet.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MtgCardInfoSpeechletTest {

    private MtgCardInfoSpeechlet mtgCardInfoSpeechlet;

    @Before
    public void setup() {
        this.mtgCardInfoSpeechlet = new MtgCardInfoSpeechlet(new MockMtgCardInfo());
    }

    // 4
    @Test
    public void shouldMaintainSessionWhenUsersLaunchesApp() throws SpeechletException {
        SpeechletResponse response = this.mtgCardInfoSpeechlet.onLaunch(launch(), session());
        assertThat(response.getShouldEndSession(), is(false));
    }

    //3
    @Test
    public void shouldExitWhenUsersSayStop() throws SpeechletException {
        SpeechletResponse response = this.mtgCardInfoSpeechlet.onIntent(stop(), session());
        assertThat(response.getShouldEndSession(), is(true));
    }

    // 3
    @Test
    public void shouldExitWhenUsersSayCancel() throws SpeechletException {
        SpeechletResponse response = this.mtgCardInfoSpeechlet.onIntent(cancel(), session());
        assertThat(response.getShouldEndSession(), is(true));
    }

    // 4
    @Test
    public void shouldMaintainSessionWhenCardIsntRecognised() throws SpeechletException {
        SpeechletResponse response = this.mtgCardInfoSpeechlet.onIntent(castingCost(), session());
        assertThat(response.getShouldEndSession(), is(false));
    }

    // 5
    @Test
    public void shouldExitWhenCardCastingCostIsRecognised() throws SpeechletException {
        SpeechletResponse response = this.mtgCardInfoSpeechlet.onIntent(castingCost("ponder"), session());
        assertThat(response.getShouldEndSession(), is(true));
    }

    // 5
    @Test
    public void shouldExitWhenCardDescriptionIsRecognised() throws SpeechletException {
        SpeechletResponse response = this.mtgCardInfoSpeechlet.onIntent(description("ponder"), session());
        assertThat(response.getShouldEndSession(), is(true));
    }

    private LaunchRequest launch() {
        return LaunchRequest.builder()
                .withRequestId(UUID.randomUUID().toString())
                .build();
    }

    private IntentRequest stop() {
        return intent(AMAZON_STOP_INTENT);
    }

    private IntentRequest cancel() {
        return intent(AMAZON_CANCEL_INTENT);
    }

    private IntentRequest castingCost() {
        return intent(INTENT_MTG_CASTING_COST);
    }

    private IntentRequest castingCost(String cardName) {
        return intent(INTENT_MTG_CASTING_COST, cardName);
    }

    private IntentRequest description(String cardName) {
        return intent(INTENT_MTG_DESCRIPTION, cardName);
    }

    private IntentRequest intent(String intent) {
        return IntentRequest.builder()
                .withIntent(Intent.builder()
                        .withName(intent)
                        .build())
                .withRequestId(UUID.randomUUID().toString())
                .build();
    }

    private IntentRequest intent(String intent, String cardName) {
        Map<String, Slot> slots = new HashMap<>();
        slots.put(CARDNAME_SLOT_KEY, Slot.builder()
                .withName(CARDNAME_SLOT_KEY)
                .withValue(cardName)
                .build());

        return IntentRequest.builder()
                .withIntent(Intent.builder()
                        .withName(intent)
                        .withSlots(slots)
                        .build())
                .withRequestId(UUID.randomUUID().toString())
                .build();
    }

    private Session session() {
        return Session.builder()
                .withSessionId(UUID.randomUUID().toString())
                .build();
    }
}
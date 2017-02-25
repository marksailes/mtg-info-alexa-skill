package net.sailes.alexa.mtg.deckbrew;

import net.sailes.alexa.mtg.CardNotFoundException;
import net.sailes.alexa.mtg.CastingCost;
import net.sailes.alexa.mtg.MtgCardInfo;
import org.apache.http.client.fluent.Request;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DeckbrewCardInfo implements MtgCardInfo {

    private static final Logger log = LoggerFactory.getLogger(DeckbrewCardInfo.class);
    public static final String KEY_NAME = "name";
    public static final String KEY_COST = "cost";
    public static final String DECKBREW_CARDS_API_URL = "https://api.deckbrew.com/mtg/cards?name=";

    @Override
    public CastingCost castingCost(String cardName) {
        try {
            JSONObject cardJson = getMatchingCardJson(cardName);
            String castingCost = cardJson.getString(KEY_COST);
            log.info("Casting cost from Deckbrew [{}]", castingCost);
            return DeckbrewCastingCostFactory.create(castingCost);
        } catch (CardNotFoundException e) {
            log.error("Deckbrew don't have the Card [{}]", cardName);
            return new CastingCost("", "We can't find the casting cost for this card.");
        }
    }

    @Override
    public String description(String cardName) {
        try {
            JSONObject cardJson = getMatchingCardJson(cardName);
            String description = cardJson.getString("text");
            log.info("Description from Deckbrew [{}]", description);
            return description;
        } catch (CardNotFoundException e) {
            log.error("Deckbrew don't have the Card [{}]", cardName);
            return "We can't find the rules for this card.";
        }
    }

    private JSONObject getMatchingCardJson(String cardName) throws CardNotFoundException {
        String responseBody = null;
        try {
            String uri = DECKBREW_CARDS_API_URL + URLEncoder.encode(cardName, StandardCharsets.UTF_8.toString());
            log.info("deckbrew url {}", uri);
            responseBody = Request.Get(uri)
                    .connectTimeout(1000)
                    .socketTimeout(1000)
                    .execute()
                    .returnContent()
                    .asString();
            log.debug(responseBody);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        JSONArray objects = new JSONArray(responseBody);

        for (int i = 0; i < objects.length(); i++)
        {
            JSONObject jsonObject = objects.getJSONObject(i);
            String jsonCardName = jsonObject.getString(KEY_NAME);
            if(cardName.equalsIgnoreCase(jsonCardName)) {
                return jsonObject;
            }
        }
        throw new CardNotFoundException("Could not find card: " + cardName);
    }
}

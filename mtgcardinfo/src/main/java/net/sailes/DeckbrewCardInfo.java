package net.sailes;

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

    public CastingCost castingCost(String cardName) {
        String castingCost;
        try {
            JSONObject cardJson = getMatchingCardJson(cardName);
            castingCost = cardJson.getString(KEY_COST);
        } catch (CardNotFoundException e) {
            castingCost = "we can't find the casting cost for this card.";
        }
        log.info("Casting cost {}", castingCost);

        return DeckbrewCastingCostFactory.create(castingCost);
    }

    @Override
    public String description(String cardName) {
        String description;
        try {
            JSONObject cardJson = getMatchingCardJson(cardName);
            description = cardJson.getString("text");
        } catch (CardNotFoundException e) {
            description = "we can't find the rules for this card.";
        }

        log.info("description {}", description);

        return description;
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
            log.info(responseBody);
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

    @Override
    public String fullInformation(String cardName) {
        return "some full information";
    }
}

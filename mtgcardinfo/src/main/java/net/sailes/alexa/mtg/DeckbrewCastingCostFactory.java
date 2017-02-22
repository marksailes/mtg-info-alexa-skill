package net.sailes.alexa.mtg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DeckbrewCastingCostFactory {

    private static final Logger log = LoggerFactory.getLogger(MtgCardInfoSpeechlet.class);

    private static final Map<String, String> NAMES = new HashMap<>();
    static {
        NAMES.put("C", "colourless");
        NAMES.put("W", "white");
        NAMES.put("U", "blue");
        NAMES.put("B", "black");
        NAMES.put("R", "red");
        NAMES.put("G", "green");
        NAMES.put("W/P", "white phyrexian");
        NAMES.put("U/P", "blue phyrexian");
        NAMES.put("B/P", "black phyrexian");
        NAMES.put("R/P", "red phyrexian");
        NAMES.put("G/P", "green phyrexian");
        NAMES.put("B/R", "black or red");
    }

    public static CastingCost create(String castingCost) {
        if (castingCost.isEmpty()) {
            return new CastingCost(castingCost, "no casting cost");
        }

        String[] split = castingCost.replace("{", "").split("}");

        StringBuilder sb = processPart(new StringBuilder(), split);

        return new CastingCost(castingCost, sb.toString());
    }

    private static StringBuilder processPart(StringBuilder sb, String[] split) {
        if (split.length < 1) {
            return sb;
        }

        String firstElement = split[0];
        log.debug("processing {}", firstElement);

        int countSimilar = 1;

        if (isNumericOrX(firstElement)) {
            sb.append(firstElement);
        } else {
            countSimilar = countSimilar(firstElement, split);

            sb.append(countSimilar)
                    .append(" ")
                    .append(parseCost(firstElement));

        }

        if (countSimilar <= split.length - 1) {
            sb.append(" and ");
            String[] newRange = Arrays.copyOfRange(split, countSimilar, split.length);
            processPart(sb, newRange);
        }

        return sb;
    }

    private static boolean isNumericOrX(String s) {
        return s.equalsIgnoreCase("x") || isNumeric(s);
    }

    private static boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    private static int countSimilar(String first, String[] split) {
        int count = 1;

        for (int i = 1; i < split.length; i++) {
            if (first.equalsIgnoreCase(split[i])){
                count++;
            }
        }
        return count;
    }

    private static String parseCost(String str) {
        if (NAMES.containsKey(str)) {
            return NAMES.get(str);
        }

        return str;
    }
}

package net.sailes;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DeckbrewCastingCostFactoryTest {

    @Test
    public void testAnnoyingCastingCost() {
        assertThat("Ponder", DeckbrewCastingCostFactory.create("{U}").text(), is("1 blue"));
        assertThat("Griselbrand", DeckbrewCastingCostFactory.create("{4}{B}{B}{B}{B}").text(), is("4 and 4 black"));
        assertThat("Noxious Revival", DeckbrewCastingCostFactory.create("{G/P}").text(), is("1 green phyrexian"));
        assertThat("Thought-Knot Seer", DeckbrewCastingCostFactory.create("{3}{C}").text(), is("3 and 1 colourless"));
        assertThat("Ancestral Vision", DeckbrewCastingCostFactory.create("").text(), is("no casting cost"));
        assertThat("Ashenmoor Gouger", DeckbrewCastingCostFactory.create("{B/R}{B/R}{B/R}").text(), is("3 black or red"));
        assertThat("Fireball", DeckbrewCastingCostFactory.create("{X}{R}").text(), is("X and 1 red"));
        assertThat("Chalice of the Void", DeckbrewCastingCostFactory.create("{X}{X}").text(), is("X and X"));
        assertThat("Cruel Ultimatum", DeckbrewCastingCostFactory.create("{U}{U}{B}{B}{B}{R}{R}").text(), is("2 blue and 3 black and 2 red"));
    }

    @Ignore("Out of scope as it's super annoying")
    @Test
    public void testReaperKingCastingCost() {
        CastingCost castingCost = DeckbrewCastingCostFactory.create("{2/W}{2/U}{2/B}{2/R}{2/G}");

        assertThat(castingCost.text(), is("wooberg or 10"));
    }
}
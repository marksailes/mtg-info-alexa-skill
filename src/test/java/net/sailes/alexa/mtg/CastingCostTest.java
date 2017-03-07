package net.sailes.alexa.mtg;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CastingCostTest {


    @Test
    public void testTextIsJustCastingCostAsText() {
        assertThat(new CastingCost("{U}", "1 blue").text(), is("1 blue"));
    }

    @Test
    public void testToStringMethodIsSensible() {
        assertThat(new CastingCost("{U}", "1 blue").toString(), is("CastingCost[encodedCastingCost={U},castingCostAsText=1 blue]"));
    }
}
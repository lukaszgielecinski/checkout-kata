package com.lukaszgielecinski.kata.pricing;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ProductPriceBuilderTest {

    @Rule
    public ExpectedException executionException = ExpectedException.none();

    @Test
    public void shouldThrowException_whenAddingMultiUnitSameNumberOfItems() throws Exception {
        // given
        executionException.expect(IllegalArgumentException.class);
        executionException.expectMessage("MultiUnit for 2 items already added.");
        // when
        new ProductPrice.ProductPriceBuilder("A", 20)
                .addMultiUnitOffer(2, 30)
                .addMultiUnitOffer(2, 35)
                .createProductPrice();
    }
}
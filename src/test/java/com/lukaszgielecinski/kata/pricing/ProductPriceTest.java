package com.lukaszgielecinski.kata.pricing;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductPriceTest {

    @Test
    public void shouldReturnZero_whenCheckingPriceOfZeroNumberOfItems() throws Exception {
        // given
        ProductPrice productPrice = new ProductPrice.ProductPriceBuilder("A", 20)
                .createProductPrice();
        // when
        double price = productPrice.getPrice(0);

        // then
        assertThat(price).isEqualTo(0);
    }

    @Test
    public void shouldReturnZero_whenCheckingPriceOfNegativeNumberOfItems() throws Exception {
        // given
        ProductPrice productPrice = new ProductPrice.ProductPriceBuilder("A", 20)
                .createProductPrice();
        // when
        double price = productPrice.getPrice(-5);

        // then
        assertThat(price).isEqualTo(0);
    }

    @Test
    public void shouldReturnPriceForASingleItem() throws Exception {
        // given
        ProductPrice productPrice = new ProductPrice.ProductPriceBuilder("A", 20)
                .createProductPrice();
        // when
        double price = productPrice.getPrice(1);

        // then
        assertThat(price).isEqualTo(20);
    }

    @Test
    public void shouldReturnUnitPriceMultiplied_whenNumberOfItemsGreaterThanOne() throws Exception {
        // given
        ProductPrice productPrice = new ProductPrice.ProductPriceBuilder("A", 20)
                .createProductPrice();
        // when
        double price = productPrice.getPrice(3);

        // then
        assertThat(price).isEqualTo(60);
    }

    @Test
    public void shouldReturnAMultiUnitPrice_whenNumberOfItemsMatches() throws Exception {
        // given
        ProductPrice productPrice = new ProductPrice.ProductPriceBuilder("A", 20)
                .addMultiUnitOffer(3, 50)
                .createProductPrice();
        // when
        double price = productPrice.getPrice(3);

        // then
        assertThat(price).isEqualTo(50);
    }

    @Test
    public void shouldReturnAMultiUnitPricePlusUnitPrice_whenNumberOfItemsIsMultiUnitUnitsPlusOne() throws Exception {
        // given
        ProductPrice productPrice = new ProductPrice.ProductPriceBuilder("A", 20)
                .addMultiUnitOffer(3, 50)
                .createProductPrice();
        // when
        double price = productPrice.getPrice(4);

        // then
        assertThat(price).isEqualTo(70);
    }
}
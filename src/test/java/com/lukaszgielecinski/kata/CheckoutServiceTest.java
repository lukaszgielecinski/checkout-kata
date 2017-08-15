package com.lukaszgielecinski.kata;

import com.lukaszgielecinski.kata.domain.Product;
import com.lukaszgielecinski.kata.pricing.ProductPrice.ProductPriceBuilder;
import org.assertj.core.util.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CheckoutServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldThrowException_whenProductPriceNotFound() throws Exception {
        // given
        CheckoutService service = new CheckoutService(Lists.newArrayList(
                new ProductPriceBuilder("C", 20).createProductPrice(),
                new ProductPriceBuilder("D", 15).createProductPrice()
        ));

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Cannot find any price for the product 'E'.");

        // when
        service.checkout(Lists.newArrayList(
                new Product("E")
        ));
    }

    @Test
    public void shouldThrowException_whenManyProductsPriceNotFound() throws Exception {
        // given
        CheckoutService service = new CheckoutService(Lists.newArrayList(
                new ProductPriceBuilder("C", 20).createProductPrice(),
                new ProductPriceBuilder("D", 15).createProductPrice()
        ));

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Cannot find any price for the products 'E,F'.");

        // when
        service.checkout(Lists.newArrayList(
                new Product("E"),
                new Product("D"),
                new Product("F"),
                new Product("E")
        ));
    }
}
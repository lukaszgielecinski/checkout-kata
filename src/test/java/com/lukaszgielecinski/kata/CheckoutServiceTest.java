package com.lukaszgielecinski.kata;

import com.lukaszgielecinski.kata.domain.Product;
import com.lukaszgielecinski.kata.pricing.ProductPrice.ProductPriceBuilder;
import org.assertj.core.util.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    public void shouldReturnZero_whenEmptyItemsCheckout() throws Exception {
        // given
        CheckoutService service = new CheckoutService(Lists.newArrayList(
                new ProductPriceBuilder("A", 50)
                        .addMultiUnitOffer(3, 130)
                        .createProductPrice(),
                new ProductPriceBuilder("B", 30)
                        .addMultiUnitOffer(2, 45)
                        .createProductPrice(),
                new ProductPriceBuilder("C", 20).createProductPrice(),
                new ProductPriceBuilder("D", 15).createProductPrice()
        ));

        // when
        double totalPrice = service.checkout(Lists.newArrayList());

        // then
        assertThat(totalPrice).isEqualTo(0);
    }

    @Test
    public void shouldReturnAPrice_whenSingleItemCheckout() throws Exception {
        // given
        CheckoutService service = new CheckoutService(Lists.newArrayList(
                new ProductPriceBuilder("A", 50)
                        .addMultiUnitOffer(3, 130)
                        .createProductPrice(),
                new ProductPriceBuilder("B", 30)
                        .addMultiUnitOffer(2, 45)
                        .createProductPrice(),
                new ProductPriceBuilder("C", 20).createProductPrice(),
                new ProductPriceBuilder("D", 15).createProductPrice()
        ));

        // when
        double totalPrice = service.checkout(Lists.newArrayList(
                new Product("C")
        ));

        // then
        assertThat(totalPrice).isEqualTo(20);
    }

    @Test
    public void shouldReturnAPrice_whenManyItemsOfSingleItemCheckout() throws Exception {
        // given
        CheckoutService service = new CheckoutService(Lists.newArrayList(
                new ProductPriceBuilder("A", 50)
                        .addMultiUnitOffer(3, 130)
                        .createProductPrice(),
                new ProductPriceBuilder("B", 30)
                        .addMultiUnitOffer(2, 45)
                        .createProductPrice(),
                new ProductPriceBuilder("C", 20).createProductPrice(),
                new ProductPriceBuilder("D", 15).createProductPrice()
        ));

        // when
        double totalPrice = service.checkout(Lists.newArrayList(
                new Product("C"),
                new Product("C"),
                new Product("C"),
                new Product("C")
        ));

        // then
        assertThat(totalPrice).isEqualTo(80);
    }

    @Test
    public void shouldReturnABundlePrice() throws Exception {
        // given
        CheckoutService service = new CheckoutService(Lists.newArrayList(
                new ProductPriceBuilder("A", 50)
                        .addMultiUnitOffer(3, 130)
                        .createProductPrice(),
                new ProductPriceBuilder("B", 30)
                        .addMultiUnitOffer(2, 45)
                        .createProductPrice(),
                new ProductPriceBuilder("C", 20).createProductPrice(),
                new ProductPriceBuilder("D", 15).createProductPrice()
        ));

        // when
        double totalPrice = service.checkout(Lists.newArrayList(
                new Product("A"),
                new Product("A"),
                new Product("A")
        ));

        // then
        assertThat(totalPrice).isEqualTo(130);
    }

    @Test
    public void shouldReturnFindABundlePriceWhenItemsMixed() throws Exception {
        // given
        CheckoutService service = new CheckoutService(Lists.newArrayList(
                new ProductPriceBuilder("A", 50)
                        .addMultiUnitOffer(3, 130)
                        .createProductPrice(),
                new ProductPriceBuilder("B", 30)
                        .addMultiUnitOffer(2, 45)
                        .createProductPrice(),
                new ProductPriceBuilder("C", 20).createProductPrice(),
                new ProductPriceBuilder("D", 15).createProductPrice()
        ));

        // when
        double totalPrice = service.checkout(Lists.newArrayList(
                new Product("B"),
                new Product("A"),
                new Product("C"),
                new Product("A"),
                new Product("D"),
                new Product("A")
        ));

        // then
        assertThat(totalPrice).isEqualTo(195);
    }
}
package com.lukaszgielecinski.kata;

import com.lukaszgielecinski.kata.domain.Product;
import com.lukaszgielecinski.kata.pricing.ProductPrice;
import com.lukaszgielecinski.kata.pricing.ProductPrice.ProductPriceBuilder;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ArrayList<ProductPrice> productPrices;
    private CheckoutService service = new CheckoutService();

    @Before
    public void setUp() throws Exception {
        productPrices = Lists.newArrayList(
                new ProductPriceBuilder("A", 50)
                        .addMultiUnitOffer(3, 130)
                        .createProductPrice(),
                new ProductPriceBuilder("B", 30)
                        .addMultiUnitOffer(2, 45)
                        .createProductPrice(),
                new ProductPriceBuilder("C", 20).createProductPrice(),
                new ProductPriceBuilder("D", 15).createProductPrice()
        );
    }

    @Test
    public void shouldThrowException_whenProductPriceNotFound() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Cannot find any price for the product 'E'.");

        ArrayList<ProductPrice> productPrices = Lists.newArrayList(
                new ProductPriceBuilder("C", 20).createProductPrice(),
                new ProductPriceBuilder("D", 15).createProductPrice()
        );

        // when
        service.checkout(Lists.newArrayList(
                new Product("E")
        ), productPrices);
    }

    @Test
    public void shouldThrowException_whenManyProductsPriceNotFound() throws Exception {
        // given
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Cannot find any price for the products 'E,F'.");

        ArrayList<ProductPrice> productPrices = Lists.newArrayList(
                new ProductPriceBuilder("C", 20).createProductPrice(),
                new ProductPriceBuilder("D", 15).createProductPrice()
        );

        // when
        service.checkout(Lists.newArrayList(
                new Product("E"),
                new Product("D"),
                new Product("F"),
                new Product("E")
        ), productPrices);
    }

    @Test
    public void shouldReturnZero_whenEmptyItemsCheckout() throws Exception {
        // given
        ArrayList<Product> products = Lists.newArrayList();

        // when
        double totalPrice = service.checkout(products, productPrices);

        // then
        assertThat(totalPrice).isEqualTo(0);
    }

    @Test
    public void shouldReturnAPrice_whenSingleItemCheckout() throws Exception {
        // given
        ArrayList<Product> products = Lists.newArrayList(
                new Product("C")
        );

        // when
        double totalPrice = service.checkout(products, productPrices);

        // then
        assertThat(totalPrice).isEqualTo(20);
    }

    @Test
    public void shouldReturnAPrice_whenManyItemsOfSingleItemCheckout() throws Exception {
        // given
        ArrayList<Product> products = Lists.newArrayList(
                new Product("C"),
                new Product("C"),
                new Product("C"),
                new Product("C")
        );

        // when
        double totalPrice = service.checkout(products, productPrices);

        // then
        assertThat(totalPrice).isEqualTo(80);
    }

    @Test
    public void shouldReturnABundlePrice() throws Exception {
        // given
        ArrayList<Product> products = Lists.newArrayList(
                new Product("A"),
                new Product("A"),
                new Product("A")
        );

        // when
        double totalPrice = service.checkout(products, productPrices);

        // then
        assertThat(totalPrice).isEqualTo(130);
    }

    @Test
    public void shouldReturnFindABundlePriceWhenItemsMixed() throws Exception {
        // given
        ArrayList<Product> products = Lists.newArrayList(
                new Product("B"),
                new Product("A"),
                new Product("C"),
                new Product("A"),
                new Product("D"),
                new Product("A")
        );

        // when
        double totalPrice = service.checkout(products, productPrices);

        // then
        assertThat(totalPrice).isEqualTo(195);
    }

    @Test
    public void shouldBeAbleToUpdateProductPrices() throws Exception {
        // given
        ArrayList<Product> listOfProducts = Lists.newArrayList(
                new Product("A"),
                new Product("A"),
                new Product("A"),
                new Product("A")

        );
        double totalPriceBeforeUpdate = service.checkout(listOfProducts, productPrices);

        ArrayList<ProductPrice> newProductPrices = Lists.newArrayList(
                new ProductPriceBuilder("A", 40)
                        .addMultiUnitOffer(4, 150)
                        .createProductPrice()
        );

        // when
        double totalPriceAfterUpdate = service.checkout(listOfProducts, newProductPrices);
        // then
        assertThat(totalPriceBeforeUpdate).isEqualTo(180);
        assertThat(totalPriceAfterUpdate).isEqualTo(150);
    }
}
package com.lukaszgielecinski.kata;

import com.lukaszgielecinski.kata.domain.Product;
import com.lukaszgielecinski.kata.pricing.ProductPrice;
import com.sun.javafx.geom.transform.Identity;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CheckoutService {

    private Map<String, ProductPrice> productPrices;

    public CheckoutService(ArrayList<ProductPrice> productPrices) {
        this.productPrices = productPrices.stream()
                .collect(Collectors.toMap(ProductPrice::getName, Function.identity()));
    }

    public double checkout(List<Product> products) {
        validateProducts(products);

        Map<String, Long> countsPerProduct = products.stream()
                .collect(Collectors.groupingBy(Product::getName, Collectors.counting()));

        return countsPerProduct.entrySet().stream()
                .mapToDouble(entry -> productPrices.get(entry.getKey()).getPrice(entry.getValue().intValue()))
                .sum();
    }

    private void validateProducts(List<Product> products) {
        List<String> unknownPriceProducts = products.stream()
                .map(Product::getName)
                .filter(product -> !productPrices.containsKey(product))
                .distinct()
                .collect(Collectors.toList());

        if (!unknownPriceProducts.isEmpty()) {
            String pluralSuffix = unknownPriceProducts.size() == 1 ? "" : "s";
            throw new IllegalArgumentException(String.format("Cannot find any price for the product%s '%s'.",
                    pluralSuffix, StringUtils.join(unknownPriceProducts, ",")));
        }
    }
}

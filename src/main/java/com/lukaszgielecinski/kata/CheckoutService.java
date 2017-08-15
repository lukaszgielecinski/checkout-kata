package com.lukaszgielecinski.kata;

import com.lukaszgielecinski.kata.domain.Product;
import com.lukaszgielecinski.kata.pricing.ProductPrice;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public class CheckoutService {


    public double checkout(List<Product> products, ArrayList<ProductPrice> productPrices) {
        Map<String, ProductPrice> currentPrices = transformProductPrices(productPrices);
        validateProducts(products, currentPrices);

        Map<String, Long> countsPerProduct = countProducts(products);

        return calculateTotalPrice(currentPrices, countsPerProduct);
    }

    private double calculateTotalPrice(Map<String, ProductPrice> currentPrices, Map<String, Long> countsPerProduct) {
        return countsPerProduct.entrySet().stream()
                .mapToDouble(calculateProductTotalPrice(currentPrices))
                .sum();
    }

    private Map<String, ProductPrice> transformProductPrices(ArrayList<ProductPrice> productPrices) {
        return productPrices.stream()
                .collect(Collectors.toMap(ProductPrice::getName, Function.identity()));
    }

    private ToDoubleFunction<Map.Entry<String, Long>> calculateProductTotalPrice(Map<String, ProductPrice> currentPrices) {
        return unitsByProduct -> {
            int numberOfUnits = unitsByProduct.getValue().intValue();
            String productName = unitsByProduct.getKey();
            return currentPrices.get(productName).getPrice(numberOfUnits);
        };
    }

    private Map<String, Long> countProducts(List<Product> products) {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getName, Collectors.counting()));
    }

    private void validateProducts(List<Product> products, Map<String, ProductPrice> currentPrices) {
        List<String> unknownPriceProducts = findUnknownPriceProducts(products, currentPrices);

        if (!unknownPriceProducts.isEmpty()) {
            String pluralSuffix = unknownPriceProducts.size() == 1 ? "" : "s";
            throw new IllegalArgumentException(String.format("Cannot find any price for the product%s '%s'.",
                    pluralSuffix, StringUtils.join(unknownPriceProducts, ",")));
        }
    }

    private List<String> findUnknownPriceProducts(List<Product> products, Map<String, ProductPrice> currentPrices) {
        return products.stream()
                .map(Product::getName)
                .filter(product -> !currentPrices.containsKey(product))
                .distinct()
                .collect(Collectors.toList());
    }
}

package com.lukaszgielecinski.kata.pricing;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class ProductPrice {

    private final String name;
    private final List<MultiUnitPrice> multiUnitPrices;

    private ProductPrice(String name, List<MultiUnitPrice> multiUnitPrices) {
        this.name = name;
        this.multiUnitPrices = multiUnitPrices;
    }

    public String getName() {
        return name;
    }

    public double getPrice(int numberOfUnits) {
        double price = 0;
        int unitsToPrice = numberOfUnits;
        while (unitsToPrice > 0) {
            MultiUnitPrice biggestMultiUnitPrice = findTheBiggestMultiItemPrice(unitsToPrice);

            price += getMultiUnitTotalPrice(unitsToPrice, biggestMultiUnitPrice);

            unitsToPrice %= biggestMultiUnitPrice.getNumberOfUnits();
        }
        return price;
    }

    private double getMultiUnitTotalPrice(int numberOfUnits, MultiUnitPrice multiUnitPrice) {
        int numberOfMultiOffers = numberOfUnits / multiUnitPrice.getNumberOfUnits();
        return numberOfMultiOffers * multiUnitPrice.getPrice();
    }


    private MultiUnitPrice findTheBiggestMultiItemPrice(int numberOfUnits) {
        return multiUnitPrices.stream()
                .sorted(Comparator.comparingInt(MultiUnitPrice::getNumberOfUnits))
                .filter(multiUnitPrice -> multiUnitPrice.getNumberOfUnits() <= numberOfUnits)
                .max(Comparator.comparingInt(MultiUnitPrice::getNumberOfUnits))
                .orElseThrow(() -> new IllegalArgumentException("At least 1 unit price should exist."));
    }

    public static class ProductPriceBuilder {
        private String name;
        private Map<Integer, Double> multiUnitPrices;

        public ProductPriceBuilder(String name, double unitPrice) {
            this.name = name;
            this.multiUnitPrices = new HashMap<>();
            this.multiUnitPrices.put(1, unitPrice);
        }

        public ProductPriceBuilder addMultiUnitOffer(int numberOfItems, double price) {
            if (multiUnitPrices.containsKey(numberOfItems)) {
                throw new IllegalArgumentException(String.format("MultiUnit for %d items already added.", numberOfItems));
            }
            multiUnitPrices.put(numberOfItems, price);
            return this;
        }

        public ProductPrice createProductPrice() {
            List<MultiUnitPrice> multiUnitPrices = this.multiUnitPrices.entrySet().stream()
                    .map(entry -> new MultiUnitPrice(entry.getKey(), entry.getValue()))
                    .collect(toList());
            return new ProductPrice(name, multiUnitPrices);
        }
    }
}

package com.lukaszgielecinski.kata.pricing;

public class MultiUnitPrice {

    private int numberOfUnits;
    private double price;

    public MultiUnitPrice(int numberOfUnits, double price) {
        this.numberOfUnits = numberOfUnits;
        this.price = price;
    }

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public double getPrice() {
        return price;
    }
}

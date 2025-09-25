package com.vaadin.starter.bakery;

/**
 * Class to test Merge function in command line
 */


public class MergerTest {

    public static double getPriceWithDiscount(double n, int percentage){
        return n*percentage/100;
    }

    public static void main(String[] args) {
        System.out.println(getPriceWithDiscount(54,10));
    }
}

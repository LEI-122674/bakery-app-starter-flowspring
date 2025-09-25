package com.vaadin.starter.bakery;

/**
 * Class to test Merge function in command line
 */


public class MergerTest {

    public static String getMyLastName(String name){
        String[] names = name.split(" ");
        return names[names.length-1];

    }

    public static void main(String[] args) {
        System.out.println("Hello World");
        String fullName = "Bianca Sofia Pestana Correia";
        System.out.println(getMyLastName(fullName));
    }

}

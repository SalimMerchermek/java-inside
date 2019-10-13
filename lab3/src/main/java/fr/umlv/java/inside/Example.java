package fr.umlv.java.inside;


import java.lang.invoke.MethodHandles;

public class Example {
    private static String aStaticHello(int value) {
        return "question " + value;
    }
    private String anInstanceHello(int value) {
        return "question " + value;
    }
}

package de.faehne.groovyinspringboot;

import groovy.lang.Closure;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DslExample {

    public static void test(final String name, final Closure c) {
        final DslTest dslTest = new DslTest(name);
        c.setDelegate(dslTest);
        c.setResolveStrategy(Closure.DELEGATE_ONLY);
        c.call();
    }
}

class DslTest {

    final PlaceHolder all = PlaceHolder.ALL;
    final PlaceHolder one = PlaceHolder.ONE;



    final ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>();
    private final String name;

    public DslTest(String name) {
        this.name = name;
    }

    public void write(final PlaceHolder ph) {
        System.out.println(ph.toString() + " - " + name);
    }

    public void settings(final Closure c) {
        c.setDelegate(map);
        c.setResolveStrategy(Closure.DELEGATE_ONLY);
        c.call();

        System.out.println(map.toString());
    }

    enum PlaceHolder {
        ALL,
        ONE
    }
}
package de.faehne.groovyinspringboot;

import java.util.HashMap;
import java.util.Map;

public class ChainObject {

    private Map<String,String> werten;

    private String singleValue;

    public ChainObject(String s) {
        this.singleValue = s;
    }

    public ChainObject(Map<String, String> werten) {
        this.werten = werten;
    }

    public static ChainObject von(Map<String,String> werten) {
        return new ChainObject(werten);
    }

    public String hole(String key) {
       return werten.get(key);
    }

    public void drucke(String key) {
        System.out.println(werten.get(key));
    }

    public void drucke(FilterAnweisung filterAnweisung) {
        switch (filterAnweisung) {
            case alles -> werten.values().forEach( val -> System.out.println(val));
            case erstes -> System.out.println(werten.values().stream().findFirst().get());
        }
    }

}

package de.faehne.groovyinspringboot;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class GroovyService {

    private final String mark = "!";

    public String getMark() {
        return mark;
    }

    public String multiplyString(String str, int repeat) {
        return StringUtils.repeat(str,repeat);
    }
}

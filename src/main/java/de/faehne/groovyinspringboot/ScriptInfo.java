package de.faehne.groovyinspringboot;

import java.util.HashMap;
import java.util.Map;

public class ScriptInfo {

    public ScriptInfo() {

    }

    public String getScript() {
        return script;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setScript(String script) {
        this.script = script;
    }

    private String script;
    private final Map<String,Object> variables = new HashMap<>();

    public ScriptInfo(String script) {
        this.script = script;
    }
}

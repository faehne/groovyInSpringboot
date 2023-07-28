package de.faehne.groovyinspringboot;


import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class GroovyController {

    private Map<String,ScriptInfo> scripts = new HashMap<>();

    private final GroovyShell shell;

    private final GroovyShell shellAll;
    private final GroovyService groovyService;

    public GroovyController(GroovyService groovyService) {
        this.groovyService = groovyService;
        SecureASTCustomizer secureASTCustomizer = new SecureASTCustomizer();
        secureASTCustomizer.setImportsWhitelist(Arrays.asList(
                "java.lang.*",
                "org.apache.commons.lang.StringUtils",
                "de.faehne.groovyinspringboot.ChainObject",
                "de.faehne.groovyinspringboot.FilterAnweisung"
        ));
        ImportCustomizer importCustomizer = new ImportCustomizer();
        importCustomizer.addImport("DoWithStrings","org.apache.commons.lang.StringUtils");
        importCustomizer.addStaticImport("de.faehne.groovyinspringboot.ChainObject","von");
        importCustomizer.addStaticImport("de.faehne.groovyinspringboot.FilterAnweisung","alles");
        importCustomizer.addStaticImport("de.faehne.groovyinspringboot.FilterAnweisung","erstes");
        importCustomizer.addStaticImport("de.faehne.groovyinspringboot.FilterAnweisung","letztes");
        CompilerConfiguration conf = new CompilerConfiguration();
        conf.addCompilationCustomizers(secureASTCustomizer);
        conf.addCompilationCustomizers(importCustomizer);
        shell = new GroovyShell(conf);

        shellAll = new GroovyShell();
    }

    @PostMapping("/scripts/direct/execute")
    public ResponseEntity executeScriptAll(@RequestBody String script) {
        return ResponseEntity.ok(shellAll.evaluate(script));
    }

    @PostMapping("/scripts/{scriptname}/execute")
    public ResponseEntity executeScript(@PathVariable String scriptname) {
        shell.getContext().getVariables().clear();
        scripts.get(scriptname).getVariables().keySet().forEach(key -> {
            shell.getContext().setProperty(
                    key,
                    scripts.get(scriptname).getVariables().get(key)
            );
        });
        shell.getContext().setProperty("groovyService",groovyService);
        Map<String,String> werte = new HashMap<>();
        return ResponseEntity.ok(shell.evaluate(scripts.get(scriptname).getScript()));
    }

    @PutMapping("/scripts/{scriptname}/savescript")
    public ResponseEntity saveScript(@RequestBody String script, @PathVariable String scriptname) {
        if(scripts.containsKey(scriptname)) {
            scripts.get(scriptname).setScript(script);
        } else {
            scripts.put(scriptname,new ScriptInfo(script));
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/scripts/{scriptname}/savevariables")
    public ResponseEntity saveVariables(@RequestBody ScriptVariablesDto variablesDto, @PathVariable String scriptname) {
        if(!scripts.containsKey(scriptname)) {
            scripts.put(scriptname,new ScriptInfo());
        }
        scripts.get(scriptname).getVariables().clear();
        variablesDto.keySet().forEach(key -> {
            scripts.get(scriptname).getVariables().put(key,variablesDto.get(key));
        });
        return ResponseEntity.noContent().build();
    }
}

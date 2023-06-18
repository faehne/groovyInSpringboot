package de.faehne.groovyinspringboot;


import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class GroovyController {

    private final GroovyShell shell;
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
        //secureASTCustomizer.
        CompilerConfiguration conf = new CompilerConfiguration();
        conf.addCompilationCustomizers(secureASTCustomizer);
        conf.addCompilationCustomizers(importCustomizer);
        shell = new GroovyShell(conf);
    }

    @PostMapping("/scripts")
    public ResponseEntity executeScript(@RequestBody String script) {
        shell.getContext().setProperty("groovyService",groovyService);
        Map<String,String> werte = new HashMap<>();
        werte.put("Salat","Nudelsalat");
        werte.put("Fleisch","Schnitzel");
        shell.getContext().setProperty("werte",werte);
        return ResponseEntity.ok(shell.evaluate(script));
    }
}

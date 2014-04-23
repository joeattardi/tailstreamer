package com.thinksincode.tailstreamer;

import joptsimple.OptionSet;

import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.JOptCommandLinePropertySource;
import org.springframework.core.env.MutablePropertySources;

public class TailStreamerApplication extends SpringApplication {
    private OptionSet options;
    
    public TailStreamerApplication(Class<?> source, OptionSet options) {
        super(source);
        this.options = options;
        setShowBanner(false);
        setAddCommandLineProperties(false);
    }
    
    @Override
    protected void configurePropertySources(ConfigurableEnvironment environment, String[] args) {
        super.configurePropertySources(environment, args);
        MutablePropertySources sources = environment.getPropertySources();
        sources.addFirst(new JOptCommandLinePropertySource(options));
    }    
}

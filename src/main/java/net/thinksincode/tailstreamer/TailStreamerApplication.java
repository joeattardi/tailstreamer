package net.thinksincode.tailstreamer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.JOptCommandLinePropertySource;
import org.springframework.core.env.MutablePropertySources;

public class TailStreamerApplication extends SpringApplication {

    public static final String MESSAGE_NO_FILE_SPECIFIED = "No file specified";
    public static final String MESSAGE_FILE_NOT_FOUND = "File not found: %s";
    public static final String MESSAGE_INVALID_OPTION = "Unknown option: %s";
    
    private OptionSet options;
    private String validationErrorMessage;
    
    public boolean parseArguments(String...args) {
        OptionParser parser = new OptionParser();
        parser.accepts("server.port").withRequiredArg();
        parser.accepts("h");
        parser.nonOptions("file to watch").ofType(File.class);
        
        try {
            options = parser.parse(args);
            return true;
        } catch (OptionException oe) {
            String firstOption = new ArrayList<String>(oe.options()).get(0);
            validationErrorMessage = String.format(MESSAGE_INVALID_OPTION, firstOption);
            return false;
        }
    }
    
    public boolean validateArguments() { 
        List<?> nonOptionArgs = options.nonOptionArguments();
        if (options.has("h")) {
            return true;
        } else if (nonOptionArgs.isEmpty()) {
            validationErrorMessage = MESSAGE_NO_FILE_SPECIFIED;
            return false;
        } else if (!((File) nonOptionArgs.get(0)).exists()) {
            validationErrorMessage = String.format(MESSAGE_FILE_NOT_FOUND, nonOptionArgs.get(0));
            return false;
        }
        
        return true;
    }
    
    public OptionSet getOptions() {
        return options;
    }
    
    public String getValidationErrorMessage() { 
        return validationErrorMessage;
    }
    
    public TailStreamerApplication(Class<?> source) {
        super(source);
        setShowBanner(false);
        setAddCommandLineProperties(false);
    }
    
    @Override
    protected void addPropertySources(ConfigurableEnvironment environment, String[] args) {
        super.addPropertySources(environment, args);
        MutablePropertySources sources = environment.getPropertySources();
        sources.addFirst(new JOptCommandLinePropertySource(options));
    }    
}

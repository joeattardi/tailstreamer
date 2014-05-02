package com.thinksincode.tailstreamer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * Parses and validates any command-line arguments specified.
 */
public class ArgumentProcessor {
    public static final String MESSAGE_NO_FILE_SPECIFIED = "No file specified";
    public static final String MESSAGE_FILE_NOT_FOUND = "File not found: %s";
    public static final String MESSAGE_READ_PERMISSION = "Unable to read \"%s\": permission denied";
    public static final String MESSAGE_INVALID_OPTION = "Unknown option: %s";
    
    private OptionSet options;
    private String validationErrorMessage;
    
    /**
     * Parses the command line arguments. This method only checks if valid arguments were specified.
     * It makes no effort to validate the values of the arguments.
     * 
     * @see #validateArguments()
     * @param args The command line arguments.
     * @return true if all the arguments were valid, false if any unknown arguments were specified.
     */
    public boolean parseArguments(String...args) {
        OptionParser parser = new OptionParser();
        parser.accepts("server.port").withRequiredArg();
        parser.accepts("h");
        parser.accepts("v");
        parser.nonOptions("file to watch");
        
        try {
            options = parser.parse(args);
            return true;
        } catch (OptionException oe) {
            String firstOption = new ArrayList<>(oe.options()).get(0);
            validationErrorMessage = String.format(MESSAGE_INVALID_OPTION, firstOption);
            return false;
        }
    }
    
    /**
     * Validates the values of the command line arguments. 
     * Before calling this method, {@link #parseArguments(String...)} must be called with the command-line arguments.
     * @return true if the argument values are valid and execution should continue, false if not.
     * @throws IllegalStateException if no arguments were previously parsed.
     */
    public boolean validateArguments() { 
        if (options == null) {
            throw new IllegalStateException("No arguments have been parsed");
        }
        List<?> nonOptionArgs = options.nonOptionArguments();
        if (options.has("h") || options.has("v")) {
            return true;
        } else if (nonOptionArgs.isEmpty()) {
            validationErrorMessage = MESSAGE_NO_FILE_SPECIFIED;
            return false;
        } 
        
        String filePath = (String) nonOptionArgs.get(0);
        if (!new File(filePath).exists()) {
            validationErrorMessage = String.format(MESSAGE_FILE_NOT_FOUND, nonOptionArgs.get(0));
            return false;
        } else if (!Files.isReadable(Paths.get(filePath))) {
            validationErrorMessage = String.format(MESSAGE_READ_PERMISSION, nonOptionArgs.get(0));
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
}

package com.thinksincode.tailstreamer;

import joptsimple.OptionSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableAsync
@EnableAutoConfiguration
@ComponentScan
public class TailStreamer implements CommandLineRunner {
    public static final String VERSION = "0.2.0";

    @Autowired
    private FileTailService fileTailService;
    
    @Value("${nonOptionArgs}")
    private String nonOptionArgs;
    
    @Override
    public void run(String...args) {
      String[] nonOptionArgsArr = nonOptionArgs.split(",");  
      fileTailService.setFile(nonOptionArgsArr[0]);
      fileTailService.tailFile();
    }

    public static void main(String...args) {
        ArgumentProcessor argumentProcessor = new ArgumentProcessor();
        if (argumentProcessor.parseArguments(args) && argumentProcessor.validateArguments()) {
            OptionSet options = argumentProcessor.getOptions();
            if (options.has("h")) {
                System.out.println(getHelpText());
            } else if (options.has("v")) {
                System.out.println("TailStreamer version " + VERSION);
            } else if (options.has("encryptPassword")) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                System.out.println(encoder.encode((String) options.nonOptionArguments().get(0)));
            } else {
                TailStreamerApplication app = new TailStreamerApplication(TailStreamer.class, argumentProcessor.getOptions());
                app.run(args);
            }
        } else {
            System.err.println(argumentProcessor.getValidationErrorMessage());
            System.out.println(getHelpText());
            System.exit(1);
        }
    }   
    
    public static String getHelpText() {
        return new StringBuilder()
        .append("Usage: tailstreamer [options] file\n")
        .append("  -h                     Print this message\n")
        .append("  -v                     Display version information\n")
        .append("  --encryptPassword      Encrypts a specified password\n")
        .append("  --server.port=PORT     Listen on PORT (default 8080)\n")
        .toString();
    }
}

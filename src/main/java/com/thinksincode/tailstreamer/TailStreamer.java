package com.thinksincode.tailstreamer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@EnableAutoConfiguration
@ComponentScan
public class TailStreamer implements CommandLineRunner {
    public static final String VERSION = "0.1.2";

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
            if (argumentProcessor.getOptions().has("h")) {
                System.out.println(getHelpText());
            } else {
                TailStreamerApplication app = new TailStreamerApplication(TailStreamer.class, argumentProcessor.getOptions());
                app.run(args);
            }
        } else {
            System.err.println(argumentProcessor.getValidationErrorMessage());
            System.exit(1);
        }
    }   
    
    public static String getHelpText() {
        StringBuilder builder = new StringBuilder();
        builder.append("Usage: tailstreamer [options] file");
        builder.append("  -h                     Print this message");
        builder.append("     --server.port=PORT  Listen on PORT (default 8080)");
        
        return builder.toString();
    }
}

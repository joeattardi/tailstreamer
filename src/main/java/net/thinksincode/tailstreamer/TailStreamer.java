package net.thinksincode.tailstreamer;

import joptsimple.OptionSet;

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
    @Autowired
    private FileTailService fileTailService;
    
    @Value("${nonOptionArgs}")
    private String nonOptionArgs;
    
    @Override
    public void run(String...args) {
      String[] nonOptionArgsArr = nonOptionArgs.split(",");  
       // TODO handle no such file here
      fileTailService.tailFile(nonOptionArgsArr[0]);
    }

    public static void main(String...args) {
        TailStreamerApplication app = new TailStreamerApplication(TailStreamer.class);
        app.parseArguments(args);
        
        if (app.parseArguments(args) && app.validateArguments()) {
            if (app.getOptions().has("h")) {
                printHelp();
            } else {
                app.run(args);
            }
        } else {
            System.err.println(app.getValidationErrorMessage());
        }
    }   
    
    private static void printHelp() {
        System.out.println("Usage: tailstreamer [options] file");
        System.out.println("  -h                     Print this message");
        System.out.println("     --server.port=PORT  Listen on PORT (default 8080)");
    }
}

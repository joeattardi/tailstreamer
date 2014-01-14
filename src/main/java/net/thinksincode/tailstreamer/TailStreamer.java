package net.thinksincode.tailstreamer;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
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
    
    @Override
    public void run(String...args) {
      fileTailService.tailFile(args[0]);
    }
    
    public static void main(String...args) {
        if (args.length < 1) {
            System.err.println("No file specified.");
            System.exit(1);
        }
        
        if (!Files.exists(Paths.get(args[0]))) {
            System.err.println(args[0] + ": File not found.");
            System.exit(1);
        }
        
        SpringApplication app = new SpringApplication(TailStreamer.class);
        app.setShowBanner(false);
        app.run(args);
    }
}

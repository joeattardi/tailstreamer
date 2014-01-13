package net.thinksincode.tailstreamer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(TailStreamer.class);
    
    @Autowired
    private FileTailService fileTailService;
    
    @Override
    public void run(String...args) {
        if (args.length < 1) {
          System.err.println("No file specified.");
          System.exit(1);
        }
        
        logger.info("Tailing file: " + args[0]);
      
      fileTailService.tailFile(args[0]);
    }
    
    public static void main(String...args) {
        SpringApplication app = new SpringApplication(TailStreamer.class);
        app.setShowBanner(false);
        app.run(args);
    }
}

package net.thinksincode.tailstreamer;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@EnableAutoConfiguration
@ComponentScan
public class TailStreamer implements CommandLineRunner {
    @Autowired
    private FileTailService fileTailService;
    
    @Autowired
    private Environment environment;
    
    @Override
    public void run(String...args) {
       String[] nonOptionArgs = environment.getProperty("nonOptionArgs").split(",");
       
       // TODO handle no such file here
      fileTailService.tailFile(nonOptionArgs[0]);
    }
    
    public static void main(String...args) {
        CommandLine commandLine = processArguments(args);
        if (commandLine == null) {
            System.exit(1);
        }
        
        if (commandLine.hasOption("h")) {
            printHelp();
            System.exit(0);
        }       
        
        String[] fileArgs = commandLine.getArgs();
                
        if (fileArgs.length < 1) {
            System.err.println("No file specified.");
            System.exit(1);
        }
        
        if (!Files.exists(Paths.get(fileArgs[0]))) {
            System.err.println(fileArgs[0] + ": File not found.");
            System.exit(1);
        }
        
        SpringApplication app = new SpringApplication(TailStreamer.class);
        app.setShowBanner(false);
        app.run(args);
    }   
    
    private static CommandLine processArguments(String...args) {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("server.port")
                            .withDescription("Listen on PORT")
                            .hasArg()
                            .withArgName("PORT")
                            .create());
        options.addOption(new Option("h", "Print this message"));
        
        CommandLineParser parser = new PosixParser();
        try {
            return parser.parse(options, args);
        } catch (ParseException pe) {
            System.err.println(pe.getMessage());
            printHelp();
            return null;
        }
    }
    
    private static void printHelp() {
        System.out.println("Usage: tailstreamer [options] file");
        System.out.println("  -h                     Print this message");
        System.out.println("     --server.port=PORT  Listen on PORT (default 8080)");
    }
}

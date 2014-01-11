package net.thinksincode.tailstreamer;

import java.io.FileNotFoundException;

public class TailStreamer {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("No file specified.");
            System.exit(1);
        }
        
        String filename = args[0];
        
        try {
            FileTail tail = new FileTail(filename);
            tail.start();
        } catch (FileNotFoundException fnfe) {
            System.err.printf("File not found: %s\n", filename);
        }
    }
}

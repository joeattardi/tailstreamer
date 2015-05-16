package com.thinksincode.tailstreamer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.thinksincode.tailstreamer.watch.FileUpdateEvent;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * The FileContentReader maintains an open file channel to the file being tailed by the
 * application. Whenever the file has been modified, the FileContentReader will read any
 * new content and broadcast it to connected clients. 
 */
@Component("fileContentReader")
public class FileContentReader implements ApplicationListener<FileUpdateEvent> {
    private Logger logger = LoggerFactory.getLogger(FileContentReader.class);
    
    public static final int BUFFER_SIZE = 1024;
    
    @Autowired
    private FileContentBroadcaster broadcaster;
    
    /** Channel for reading text from the file. */
    private FileChannel channel;
    
    /**
     * Opens a file, and seeks to the end.
     * @param file the file to open
     */
    public void openFile(final Path file) {
        try {            
            channel = FileChannel.open(file, StandardOpenOption.READ);

            // If the file is not empty, seek to the end.
            if (channel.size() > 0) {
                channel.position(channel.size() - 1);
            }
        } catch (IOException e) {
            logger.error(String.format("Error opening \"%s\": %s", file, e.getMessage()), e);
        }        
    }

    /**
     * Reads any new content that has been written to the file since the last read.
     * @return an array of Strings, one element for each new line read
     * @throws IOException if an error occurred while reading from the file
     */
    String[] readNewContent() throws IOException {
        // Check for truncation. If the file was truncated, 
        // reset the position to 0.
        if (channel.position() > channel.size()) {
            channel.position(0);
        }
        
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        StringBuilder builder = new StringBuilder();
        
        int bytesRead;
        while ((bytesRead = channel.read(buffer)) > 0) {                                   
            byte[] bytes = new byte[bytesRead];
            buffer.rewind();
            buffer.get(bytes);
            builder.append(sanitizeText(new String(bytes)));
            buffer.rewind();
        }

        return builder.toString().trim().split("\n|\r\n");
    }
    
    /**
     * Escapes entities in text before displaying them in the browser.
     * @param text the text to sanitize
     * @return the sanitized text
     */
    private String sanitizeText(final String text) {
        return StringEscapeUtils.escapeHtml4(text);
    }
    
    /**
     * Handles a FileUpdateEvent that was received. 
     * This method will read the new content, then broadcast it to clients.
     */
    @Override
    public void onApplicationEvent(FileUpdateEvent event) {
        try {
            String[] lines = readNewContent();
            broadcaster.sendContent(lines);
        } catch (IOException ioe) {
            logger.error("Error reading file content: " + ioe.getMessage(), ioe);
        } 
    }
    
    public void close() {
        try {
            channel.close();
        } catch (IOException e) {
            logger.error("Error while closing file: " + e.getMessage(), e);
        }
    }
}

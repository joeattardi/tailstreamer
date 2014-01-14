package net.thinksincode.tailstreamer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component("fileContentReader")
public class FileContentReader implements ApplicationListener<FileUpdateEvent> {
    
    public static final int BUFFER_SIZE = 1024;
    
    @Autowired
    private SimpMessagingTemplate template;
    
    /** Channel for reading text from the file. */
    private FileChannel channel;
    
    public void openFile(final Path file) {
        try {
            channel = FileChannel.open(file, StandardOpenOption.READ);
            channel.position(channel.size() - 1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
    }

    @Override
    public void onApplicationEvent(FileUpdateEvent event) {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        StringBuilder builder = new StringBuilder();
        
        try {      
            int bytesRead = 0;
            while ((bytesRead = channel.read(buffer)) > 0) {                                   
                byte[] bytes = new byte[bytesRead];
                buffer.rewind();
                buffer.get(bytes);
                builder.append(new String(bytes));
            }
            
            String[] lines = builder.toString().split("\n|\r|\r\n");
            for (String line : lines) {
                template.convertAndSend("/topic/log", line);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            // TODO handle exception
        }        
    }
}

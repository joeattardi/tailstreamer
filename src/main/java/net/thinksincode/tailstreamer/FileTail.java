package net.thinksincode.tailstreamer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Observable;
import java.util.Observer;

/**
 * Monitors a file and notifies the application when new content is added to the file. 
 */
public class FileTail implements Observer {

    public static final int BUFFER_SIZE = 1024;

    private Charset charset = Charset.defaultCharset();
    
    /** The file being tailed. */
    private Path file;

    /** Channel for reading text from the file. */
    private FileChannel channel;
    
    private FileWatcher watcher;
    
    /**
     * Constructs a FileTail object that will tail a given file.
     * @param filePath The path of the file to tail.
     * @throws FileNotFoundException if the file does not exist
     */
    public FileTail(final String filePath) throws FileNotFoundException {
        file = Paths.get(filePath);
        if (!Files.exists(file)) {
            throw new FileNotFoundException(filePath);
        }        
    }
    
    /**
     * Starts tailing the file. A new FileWatcher is created and the
     * FileTail instance is added as an Observer.
     */
    public void start() {
        
        try {
            channel = FileChannel.open(file, StandardOpenOption.READ);
            channel.position(channel.size() - 1);            
        } catch (IOException ioe) {
            ioe.printStackTrace();
            close();
        }
        
        watcher = new FileWatcher(file);
        watcher.addObserver(this);
        watcher.watchFile();
    }
    
    /**
     * Closes the file channel.
     */
    public void close() {
        try {
            channel.close();
            watcher.interrupt();
        } catch (IOException ioe) {
            // TODO handle exception
        }
    }
    
    /**
     * Called when the file is updated. This method will
     * broadcast the new content added to the file.
     */
    @Override
    public void update(Observable o, Object arg) {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        
        try {      
            int bytesRead = 0;
            while ((bytesRead = channel.read(buffer)) > 0) {                                   
                byte[] bytes = new byte[bytesRead];
                buffer.rewind();
                buffer.get(bytes);
                System.out.print(new String(bytes));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            // TODO handle exception
        }
    }
}

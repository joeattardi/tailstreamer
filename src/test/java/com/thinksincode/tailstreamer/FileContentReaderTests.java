package com.thinksincode.tailstreamer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FileContentReaderTests {
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Test
    public void testReadNewContent() throws IOException {
        File file = folder.newFile();
        
        FileWriter writer = new FileWriter(file);
        writer.write("First line\n");
        writer.flush();
        
        FileContentReader reader = new FileContentReader();
        reader.openFile(file.toPath());
        
        writer.write("Second line\nThird line\r\nFourth line\n");
        writer.close();
        
        Assert.assertArrayEquals(new String[] {"Second line", "Third line", "Fourth line"},
                reader.readNewContent());
        
        reader.close();
    }

    @Test
    public void testEmptyFile() throws IOException {
        File file = folder.newFile();
        FileContentReader reader = new FileContentReader();
        reader.openFile(file.toPath());

        FileWriter writer = new FileWriter(file);
        writer.write("First line\nSecond line");
        writer.close();

        Assert.assertArrayEquals(new String[] {"First line", "Second line"}, reader.readNewContent());
        reader.close();
    }
}

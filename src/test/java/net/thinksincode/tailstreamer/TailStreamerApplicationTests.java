package net.thinksincode.tailstreamer;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class TailStreamerApplicationTests {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Test
    public void testArgumentParsing() throws IOException {
        TailStreamerApplication app = new TailStreamerApplication(TailStreamer.class);
        
        // file option is required
        Assert.assertTrue(app.parseArguments());
        Assert.assertFalse(app.validateArguments());
        Assert.assertEquals(TailStreamerApplication.MESSAGE_NO_FILE_SPECIFIED, app.getValidationErrorMessage());
        
        Assert.assertTrue(app.parseArguments("--server.port=8000"));
        Assert.assertFalse(app.validateArguments());
        Assert.assertEquals(TailStreamerApplication.MESSAGE_NO_FILE_SPECIFIED, app.getValidationErrorMessage());
        
        Assert.assertTrue(app.parseArguments("some_file.txt"));
        Assert.assertFalse(app.validateArguments());
        Assert.assertEquals(String.format(TailStreamerApplication.MESSAGE_FILE_NOT_FOUND, "some_file.txt"), 
                app.getValidationErrorMessage());
        
        File file = folder.newFile();
        Assert.assertTrue(app.parseArguments(file.getAbsolutePath()));
        Assert.assertTrue(app.validateArguments());
        
    }
    
}

package net.thinksincode.tailstreamer;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ArgumentProcessorTests {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Test
    public void testNoArguments() {
        ArgumentProcessor processor = new ArgumentProcessor();
        Assert.assertTrue(processor.parseArguments());
        Assert.assertFalse(processor.validateArguments());
        Assert.assertEquals(ArgumentProcessor.MESSAGE_NO_FILE_SPECIFIED, processor.getValidationErrorMessage());
    }
    
    @Test
    public void testMissingFileArgument() {
        ArgumentProcessor processor = new ArgumentProcessor();
        Assert.assertTrue(processor.parseArguments("--server.port=8000"));
        Assert.assertFalse(processor.validateArguments());
        Assert.assertEquals(ArgumentProcessor.MESSAGE_NO_FILE_SPECIFIED, processor.getValidationErrorMessage());        
    }
    
    @Test
    public void testInvalidOption() {
        ArgumentProcessor processor = new ArgumentProcessor();
        Assert.assertFalse(processor.parseArguments("-z"));
        Assert.assertEquals(String.format(ArgumentProcessor.MESSAGE_INVALID_OPTION, "z"), processor.getValidationErrorMessage());
    }

    @Test
    public void testNonExistentFile() {
        ArgumentProcessor processor = new ArgumentProcessor();
        Assert.assertTrue(processor.parseArguments("some_file.txt"));
        Assert.assertFalse(processor.validateArguments());
        Assert.assertEquals(String.format(ArgumentProcessor.MESSAGE_FILE_NOT_FOUND, "some_file.txt"), 
                processor.getValidationErrorMessage());        
    }
    
    @Test
    public void testFileArgument() throws IOException {
        ArgumentProcessor processor = new ArgumentProcessor();
        File file = folder.newFile();
        Assert.assertTrue(processor.parseArguments(file.getAbsolutePath()));
        Assert.assertTrue(processor.validateArguments());
    }
}

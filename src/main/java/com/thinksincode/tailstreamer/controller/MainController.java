package com.thinksincode.tailstreamer.controller;

import com.thinksincode.tailstreamer.FileTailService;
import com.thinksincode.tailstreamer.TailStreamer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    /** The maximum filename length before truncation occurs. */
    private static final int MAX_FILENAME_LENGTH = 50;

    @Autowired
    private FileTailService fileTailService;
    
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("file", maybeTruncate(fileTailService.getFileName()));
        model.addAttribute("filePath", fileTailService.getFilePath());
        model.addAttribute("version", TailStreamer.VERSION);
        return "index";
    }

    private String maybeTruncate(String fileName) {
        if (fileName.length() <= MAX_FILENAME_LENGTH) {
            return fileName;
        } else {
            String extension = fileName.substring(fileName.lastIndexOf('.'));
            return fileName.substring(0, MAX_FILENAME_LENGTH) + "..." + fileName.substring(fileName.length() - extension.length() - 1);
        }
    }
}

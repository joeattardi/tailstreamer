package com.thinksincode.tailstreamer.controller;

import com.thinksincode.tailstreamer.FileTailService;
import com.thinksincode.tailstreamer.TailStreamer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    
    @Autowired
    private FileTailService fileTailService;
    
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("file", fileTailService.getFileName());
        model.addAttribute("version", TailStreamer.VERSION);
        return "index";
    }
}

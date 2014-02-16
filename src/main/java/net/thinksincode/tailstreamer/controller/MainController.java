package net.thinksincode.tailstreamer.controller;

import net.thinksincode.tailstreamer.FileTailService;

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
        model.addAttribute("file", fileTailService.getFile());
        return "index";
    }
}

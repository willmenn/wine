package io.wine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApplicationController {

    @RequestMapping("/")
    public String rootRedirect(){
        return "redirect:/swagger-ui.html";
    }
}

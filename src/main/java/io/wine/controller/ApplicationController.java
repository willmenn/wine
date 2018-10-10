package io.wine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class ApplicationController {

    @RequestMapping("/")
    public String rootRedirect(){
        return "index.html";
    }
}

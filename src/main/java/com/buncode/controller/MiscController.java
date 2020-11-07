package com.buncode.controller;

import com.buncode.exception.YouShallNotPassException;
import com.buncode.model.AdminConfig;
import com.buncode.service.IAdminConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.MessageFormat;

@Controller
public class MiscController {


    /*@Autowired
    BuildProperties buildProperties;

    @GetMapping("/")
    public String root(Model model) {

        //todo model.addAttribute("build", "V2");
        return  "index.html";
    }
    */


}

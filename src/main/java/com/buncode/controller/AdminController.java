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
public class AdminController {

    @Autowired
    private IAdminConfigService adminConfigService;

    @GetMapping("/showAdminPage")
    public String showAdminPage(Model model) throws YouShallNotPassException {

        AdminConfig config = adminConfigService.getAdminConfig();
        model.addAttribute("adminConfig", config);

        return "showAdminPage";
    }

    @PostMapping("/toggleMatchLock")
    @ResponseBody
    public String toggleMatchLock(boolean match_lock) throws YouShallNotPassException {

        AdminConfig config = adminConfigService.getAdminConfig();

        synchronized (config) {

            config.setMatch_lock(!config.isMatch_lock()); //toggle
            adminConfigService.save(config);
        }
        return (MessageFormat.format("<h2>Admin Match Lock updated from [{0}] to [{1}] successfully</h2>", match_lock, config.isMatch_lock()));
    }

}

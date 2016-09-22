package net.kailyard.manager.web;

import net.kailyard.common.security.SecurityUtil;
import net.kailyard.manager.security.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 基本功能controller
 */
@Controller
public class IndexController {
    @Autowired
    private AuthorityService authorityService;

    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("menuList", authorityService.getMenuTree());
        return "index";
    }

    @RequestMapping("/test")
    public String test(Model model) {
        model.addAttribute("menuList", authorityService.getMenuTree());
        return "test";
    }

    //    @RequestMapping("/menus")
//    public String menus(Model model) {
//        model.addAttribute("menuList", authorityService.getMenuTree());
//
//        return "menu";
//    }

    @RequestMapping(value = "/currentUser", method = RequestMethod.GET)
    public String north(Model model) {
        model.addAttribute("user", SecurityUtil.getCurrentUser());

        return "top";
    }

    @RequestMapping("/center")
    public String center() {
        return "center";
    }

}

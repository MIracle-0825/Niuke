package com.ding.niuke.controller;

import com.ding.niuke.entity.User;
import com.ding.niuke.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    @GetMapping(value = "/register")
    public String getRegisterPage(){
        return "/site/register";
    }
    @PostMapping(value = "/register")
    public String register(Model model ,User user){
        Map<String, Object> map = userService.register(user);
        if(map==null||map.isEmpty()){
            model.addAttribute("msg","注册成功，请查询激活邮件并及时激活");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }
        else {
            model.addAttribute("usernameMessage",map.get("usernameMessage"));
            model.addAttribute("passwordMessage",map.get("passwordMessage"));
            model.addAttribute("emailMessage",map.get("emailMessage"));
            return "/site/register";
        }
    }
}

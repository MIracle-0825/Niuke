package com.ding.niuke.controller;

import com.ding.niuke.entity.User;
import com.ding.niuke.service.UserService;
import com.ding.niuke.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    private UserService userService;
    @GetMapping(value = "/register")
    public String getRegisterPage(){
        return "/site/register";
    }
    @GetMapping(value = "/login")
    public String getLoginPage(){
        return "/site/login";
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
    @GetMapping(path = "/activation/{userId}/{code}")
    public String activation(Model model, @PathVariable("userId") int userId,@PathVariable("code") String code){
        int result = userService.activation(userId, code);
        if(result==ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功，您的账号可以正常使用！");
            model.addAttribute("target","/login");
        }
        else if(result==ACTIVATION_REPEAT){
            model.addAttribute("msg","该账号已激活！");
            model.addAttribute("target","/index");
        }
        else {
            model.addAttribute("msg","激活失败！");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }
}

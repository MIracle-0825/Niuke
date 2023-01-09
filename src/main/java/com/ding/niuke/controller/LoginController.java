package com.ding.niuke.controller;

import com.ding.niuke.entity.User;
import com.ding.niuke.service.LoginTicketService;
import com.ding.niuke.service.UserService;
import com.ding.niuke.util.CommunityConstant;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
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
    @Autowired
    private Producer kaptchaProducer;
    @GetMapping(path = "/kaptcha")
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        //生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);
        //将验证码存入session
        session.setAttribute("kaptcha",text);
        //将图片输出给浏览器
        response.setContentType("image/png");
        try {
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
           logger.error("响应验证失败"+e.getMessage());
        }
    }
    @Autowired
    private LoginTicketService loginTicketService;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @PostMapping(value = "/login")
    public String login(String username,String password,String code,boolean rememberMe,
                      Model model,HttpSession session,HttpServletResponse response){
        String kaptcha = (String)session.getAttribute("kaptcha");
        if(StringUtils.isBlank(kaptcha)||StringUtils.isBlank(code)||!code.equalsIgnoreCase(kaptcha)){
            model.addAttribute("codeMsg","验证码不正确！");
            return "/site/login";
        }
        int expiredSeconds = rememberMe ? DEFAULT_EXPIRED_SECONDS : REMEMBER_EXPIRED_SECONDS;
        Map<String, Object> map = loginTicketService.login(username, password, expiredSeconds);
        if(map.containsKey("ticket")){
            String ticket = (String)map.get("ticket");
            ticket.replaceAll(" ","");
            Cookie cookie = new Cookie("ticket",ticket);
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }
        else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        }
    }
    @GetMapping(value = "/logout")
    public String logout(@CookieValue("ticket") String ticket){
        loginTicketService.logOut(ticket);
        return "redirect:/login";
    }
}

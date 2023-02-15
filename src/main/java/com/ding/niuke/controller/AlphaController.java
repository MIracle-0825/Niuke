package com.ding.niuke.controller;

import com.ding.niuke.util.CommunityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/alpha")
public class AlphaController {
    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){
        //获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        System.out.println(request.getParameter("code"));
        //返回响应数据
        response.setContentType("text/html;charset=utf-8");

        try (
                PrintWriter writer = response.getWriter();
                ) {
            writer.write("<h1>牛客<h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @GetMapping(path = "/student")
    @ResponseBody
    public String getStudents(@RequestParam(name = "current,",required = false,defaultValue = "1")int current,
                              @RequestParam(name = "limit,",required = false,defaultValue = "10")int limit){
        System.out.println(current);
        System.out.println(limit);
        return "the properties of students";
    }
    @PostMapping(value = "/students")
    @ResponseBody
    public String getStudentInfo(String name,String age){
        System.out.println(name);
        System.out.println(age);
        return "student";
    }
    @GetMapping(value = "/teacher")
    public ModelAndView getTeacher(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("name","Li");
        mav.addObject("age","33");
        mav.setViewName("demo/view");
        return mav;
    }
    @GetMapping(value = "/school")
    public String getSchool(Model model){
        model.addAttribute("name","XDU");
        model.addAttribute("age","79");
        return "/demo/view";
    }
    //响应Json数据(Java对象->JSON字符串->JS对象)
    @GetMapping(value = "emp")
    @ResponseBody
    public Map<String,Object> getEmp(){
        HashMap<String, Object> emp = new HashMap<>();
        emp.put("name","Zhang");
        emp.put("age",23);
        emp.put("salary",8000);
        return emp;
    }
    @GetMapping(value = "emps")
    @ResponseBody
    public List<Map<String,Object>> getEmps(){
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        HashMap<String, Object> emp = new HashMap<>();
        emp.put("name","Zhang");
        emp.put("age",23);
        emp.put("salary",8000);
        list.add(emp);
        emp = new HashMap<>();
        emp.put("name","Li");
        emp.put("age",24);
        emp.put("salary",11000);
        list.add(emp);
        emp = new HashMap<>();
        emp.put("name","Wang");
        emp.put("age",25);
        emp.put("salary",9000);
        list.add(emp);
        return list;
    }
    //cookie示例
    @GetMapping(value = "/cookie/set")
    @ResponseBody
    public String setCookie(HttpServletResponse response){
        //cookie的value不能有空格
        Cookie cookie = new Cookie("code", "CommunityUtils.generateUUID()");
        cookie.setPath("/niuke/alpha");
        cookie.setMaxAge(60*10);
        response.addCookie(cookie);
        return "set cookie";
    }
    @GetMapping(value = "/cookie/get")
    @ResponseBody
    public String getCookie(@CookieValue("code") String cookieValue){
        System.out.println(cookieValue);
        return "get cookie";
    }
    //ajax示例
    @PostMapping(value = "/ajax")
    @ResponseBody
    public String testForAjax(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return CommunityUtils.getJSONString(0,"操作成功！");
    }
}

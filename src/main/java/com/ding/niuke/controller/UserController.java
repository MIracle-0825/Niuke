package com.ding.niuke.controller;

import com.ding.niuke.annotation.LoginRequired;
import com.ding.niuke.entity.User;
import com.ding.niuke.service.LikeService;
import com.ding.niuke.service.UserService;
import com.ding.niuke.util.CommunityUtils;
import com.ding.niuke.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${niuke.path.upload}")
    private String uploadPath;
    @Value("${niuke.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private LikeService likeService;
    @LoginRequired
    @GetMapping(value = "/setting")
    public String getSettingPage(){
        return "site/setting";
    }

    @LoginRequired
    @PostMapping(value = "/upload")
    public String uploadHeader(MultipartFile headerImage, Model model){
        if(headerImage==null){
            model.addAttribute("error","没有选择图片！");
            return "site/setting";
        }
        //截取上传文件的后缀名
        String originalFilename = headerImage.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件格式不正确！");
            return "site/setting";
        }
        //生成随机的文件名
        originalFilename = CommunityUtils.generateUUID() + suffix;
        //确定文件存放路径
        File dest = new File(uploadPath + "/" + originalFilename);
        try {
            //存储文件
            FileUtils.copyInputStreamToFile(headerImage.getInputStream(),dest);
            //headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败"+e.getMessage());
            throw new RuntimeException(("上传文件失败，服务器异常！")+e);
        }
        //更新用户图像路径
        //http://localhost:8080/niuke/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + originalFilename;
        userService.updateHeader(user.getId(),headerUrl);
        return "redirect:/index";
    }

    //获取头像
    @GetMapping(value = "/header/{originalFilename}")
    public void getHeader(@PathVariable("originalFilename") String fileName, HttpServletResponse response){
        //服务器存放路径
        fileName = uploadPath + "/" + fileName;
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //响应图片
        response.setContentType("image/"+suffix);
        try (
                //文件读取的输入流
                FileInputStream fis = new FileInputStream(fileName);
                ){
            OutputStream os = response.getOutputStream();
            //建立缓冲区
            byte[] buffer = new byte[1024];
            int b = 0;
            while((b = fis.read(buffer))!=-1){
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
           logger.error("读取头像失败："+e.getMessage());
        }
    }
    //个人主页
    @GetMapping(value = "/profile/{userId}")
    public String getProfilePage(@PathVariable("userId") int userId,Model model){
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user",user);
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount",likeCount);
        return "/site/profile";
    }
}

package com.ding.niuke.controller;

import com.ding.niuke.entity.DiscussPost;
import com.ding.niuke.entity.Page;
import com.ding.niuke.entity.User;
import com.ding.niuke.service.DiscussPostService;
import com.ding.niuke.service.LikeService;
import com.ding.niuke.service.UserService;
import com.ding.niuke.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;

    @GetMapping(value = "/index")
    public String getIndexPage(Model model,Page page){
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        if(list != null){
            for(DiscussPost post:list){
                HashMap<String, Object> map = new HashMap<>();
                map.put("post",post);
                User user = userService.findUserById(post.getUserId());
                map.put("user",user);
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount",likeCount);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        return "/index";
    }
    @GetMapping(value = "/error")
    public String getErrorPage(){
        return "/error/500";
    }
    @GetMapping(value = "/denied")
    public String getDeniedPage(){
        return "/error/404";
    }
}

package com.ding.niuke.controller;

import com.ding.niuke.entity.User;
import com.ding.niuke.service.FollowService;
import com.ding.niuke.util.CommunityUtils;
import com.ding.niuke.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FollowController {
    @Autowired
    private FollowService followService;
    @Autowired
    private HostHolder hostHolder;
    @PostMapping(value = "/follow")
    @ResponseBody
    public String follow(int entityType,int entityId){
        User user = hostHolder.getUser();
        followService.follow(user.getId(),entityType,entityId);
        return CommunityUtils.getJSONString(0,"已关注");
    }
    @PostMapping(value = "/unfollow")
    @ResponseBody
    public String unfollow(int entityType,int entityId){
        User user = hostHolder.getUser();
        followService.unfollow(user.getId(),entityType,entityId);
        return CommunityUtils.getJSONString(0,"已取消关注");
    }
}

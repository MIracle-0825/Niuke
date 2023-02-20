package com.ding.niuke.service;

import com.ding.niuke.entity.LoginTicket;
import com.ding.niuke.entity.User;
import com.ding.niuke.mapper.LoginTicketMapper;
import com.ding.niuke.mapper.UserMapper;
import com.ding.niuke.util.CommunityUtils;
import com.ding.niuke.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginTicketService {
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    public Map<String,Object> login(String username ,String password,int expiredSeconds){
        HashMap<String, Object> map = new HashMap<>();
        //空值处理
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","账号不能为空！");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空！");
            return map;
        }
        User user = userMapper.selectByName(username);
        if(user==null){
            map.put("usernameMsg","该账号不存在！");
            return map;
        }
        if(user.getStatus()==0){
            map.put("usernameMsg","账号未激活！");
            return map;
        }
       password = CommunityUtils.md5(password+user.getSalt());
        if(!password.equals(user.getPassword())){
            map.put("passwordMsg","密码错误！");
            return map;
        }
        //生成登陆凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtils.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+expiredSeconds*1000));
        //loginTicketMapper.insertLoginTicket(loginTicket);
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey,loginTicket);
        map.put("ticket",loginTicket.getTicket());
        return map;
    }
    public void logOut(String ticket){
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey,loginTicket);
        //loginTicketMapper.updateStatus(ticket,1);
    }
}

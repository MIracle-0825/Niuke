package com.ding.niuke.service;

import com.ding.niuke.entity.LoginTicket;
import com.ding.niuke.entity.User;
import com.ding.niuke.mapper.LoginTicketMapper;
import com.ding.niuke.mapper.UserMapper;
import com.ding.niuke.util.CommunityConstant;
import com.ding.niuke.util.CommunityUtils;
import com.ding.niuke.util.MailClient;
import com.ding.niuke.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${niuke.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    public User findUserById(int id){
        User user = getCache(id);
        if(user==null){
            user = initCache(id);
        }
        return user;
        //return userMapper.selectById(id);
    }
    public Map<String,Object> register(User user){
        HashMap<String, Object> map = new HashMap<>();
        if(user==null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMessage","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMessage","密码不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMessage","邮箱不能为空");
            return map;
        }
        //验证账号是否已存在
        User u = userMapper.selectByName(user.getUsername());
        if(u!=null){
            map.put("usernameMessage","该账号已存在");
            return map;
        }
        //验证邮箱
        User u1 = userMapper.selectByEmail(user.getEmail());
        if(u1!=null){
            map.put("emailMessage","该邮箱已存在");
            return map;
        }
        //用户注册
        user.setSalt(CommunityUtils.generateUUID().substring(0,5));
        user.setStatus(0);
        user.setType(0);
        user.setPassword(CommunityUtils.md5(user.getPassword()+user.getSalt()));
        user.setActivationCode(CommunityUtils.generateUUID());
        //String类的format()方法用于创建格式化的字符串以及连接多个字符串对象。
        user.setHeaderUrl(String.format("http://images.nowcoder/head/%dt,png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        //激活邮件
        //将属性传给thymeleaf
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        String url = domain + contextPath + "/activation/" +user.getId() +"/"+ user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("mail/activation", context);
        mailClient.sendMail(user.getEmail(),"activation",content);
        return map;
    }
    //激活方法
    public int activation(int userId,String code){
        User user = userMapper.selectById(userId);
        if(user.getStatus()==1){
            return ACTIVATION_REPEAT;
        }
        else if(user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId,1);
            clearCache(userId);
            return ACTIVATION_SUCCESS;
        }
        else {
            return ACTIVATION_FAILURE;
        }
    }
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    public LoginTicket findLoginTicket(String ticket){
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }
    public int updateHeader(int userId,String headerUrl){
        int rows = userMapper.updateHeader(userId, headerUrl);
        clearCache(userId);
        return rows;
    }
    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }
    //1.优先从缓存中取值
    private User getCache(int userId){
        String redisKey = RedisKeyUtil.getUserKey(userId);
        User user = (User)redisTemplate.opsForValue().get(redisKey);
        return user;
    }
    //2.取不到时更新缓存数据
    private User initCache(int userId){
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey,user,3600, TimeUnit.SECONDS);
        return user;
    }
    //3.数据变更时清除缓存数据
    private void clearCache(int userId){
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }
    public Collection<?extends GrantedAuthority> getAuthorities(int userId){
        User user = this.findUserById(userId);
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (user.getType()){
                    case 1:
                        return AUTHORITY_ADMIN;
                    case 2:
                        return AUTHORITY_MODERATOR;
                    default:
                        return AUTHORITY_USER;
                }
            }
        });
        return list;
    }
}

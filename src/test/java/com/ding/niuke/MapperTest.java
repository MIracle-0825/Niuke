package com.ding.niuke;

import com.ding.niuke.entity.DiscussPost;
import com.ding.niuke.entity.LoginTicket;
import com.ding.niuke.entity.User;
import com.ding.niuke.mapper.DiscussPostMapper;
import com.ding.niuke.mapper.LoginTicketMapper;
import com.ding.niuke.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@SpringBootTest
//@RunWith(SpringRunner.class)
//以NiukeApplication为配置类运行代码
@ContextConfiguration(classes = NiukeApplication.class)
public class MapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Test
    public void testForSelectUser(){
        System.out.println(userMapper.selectById(101));
    }
    @Test
    public void testSelectPosts(){
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149, 0, 10);
        list.forEach(System.out::println);
        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }
    @Test
    public void testForInsert(){
        User user = new User();
        user.setUsername("aaa12");
        user.setPassword("sdasdaa");
        user.setEmail("2311@111.com");
        userMapper.insertUser(user);
        int id = user.getId();
        System.out.println(id);
    }
    @Test
    public void testForUpDate(){
        userMapper.updateStatus(157,1);
        User user = userMapper.selectById(157);
        System.out.println(user);
    }
    @Test
    public void testForUpdate1(){
        userMapper.updatePassword(157,"123eee");
        System.out.println(userMapper.selectById(157));
    }
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Test
    public void testForInsertLoginTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(12);
        loginTicket.setTicket("012");
        loginTicket.setStatus(1);
        loginTicket.setExpired(new Date(System.currentTimeMillis()));
        loginTicketMapper.insertLoginTicket(loginTicket);
        LoginTicket log1 = loginTicketMapper.selectByTicket("012");
        System.out.println(log1);
    }
    @Test
    public void testForUpdateHeaderUrl(){
        userMapper.updateHeader(158,"http://images.nowcoder/head/722t,png");
        System.out.println(userMapper.selectById(158));
    }
    @Test
    public void testForS(){
        DiscussPost post = new DiscussPost();
        post.setContent("111");
        post.setUserId(168);
        post.setTitle("xx");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);
    }
}

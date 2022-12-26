package com.ding.niuke;

import com.ding.niuke.entity.DiscussPost;
import com.ding.niuke.entity.User;
import com.ding.niuke.mapper.DiscussPostMapper;
import com.ding.niuke.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

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
}

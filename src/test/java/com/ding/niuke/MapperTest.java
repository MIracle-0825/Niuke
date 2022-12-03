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
}

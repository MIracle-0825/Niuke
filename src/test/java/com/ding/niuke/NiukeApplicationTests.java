package com.ding.niuke;

import com.ding.niuke.config.AlphaConfig;
import com.ding.niuke.dao.AlphaDao;
import com.ding.niuke.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
//@RunWith(SpringRunner.class)
//以NiukeApplication为配置类运行代码
@ContextConfiguration(classes = NiukeApplication.class)
public class NiukeApplicationTests implements ApplicationContextAware {
    @Autowired
    private AlphaDao alphaDao;
    @Autowired
    private AlphaConfig alphaConfig;
    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    @Test
    public void contextLoads() {
        System.out.println(alphaDao.select());
       /* AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);
        System.out.println(alphaDao.select());*/
    }
    @Test
    public void testBeanConfig(){
        SimpleDateFormat simpleDateFormat = alphaConfig.simpleDateFormat();
        String date = simpleDateFormat.format(new Date());
        System.out.println(date);
    }
}

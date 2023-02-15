package com.ding.niuke;

import com.ding.niuke.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
//@RunWith(SpringRunner.class)
//以NiukeApplication为配置类运行代码
@ContextConfiguration(classes = NiukeApplication.class)
public class SensitiveTests {
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Test
    public void testForSensitiveFilter(){
        String t = "这里可以赌博，吸毒";
        String text = sensitiveFilter.filter(t);
        System.out.println(text);
    }
}

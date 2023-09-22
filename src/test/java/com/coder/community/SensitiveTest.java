package com.coder.community;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.coder.community.entity.DiscussPost;
import com.coder.community.service.DiscussPostService;
import com.coder.community.util.SensitiveFilter;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SensitiveTest {
    
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private DiscussPostService discussPostService;

    @Test
    public void testSensitiveFilter() {
        String text = "中国要赌博，我要喝酒，我要吸毒，China必胜！";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
        
        text = "喝酒开票";
        text = sensitiveFilter.filter(text);
        System.out.println(text);

        DiscussPost post = discussPostService.findDiscussPostById(291);
        System.out.println(post.getTitle());
        post.setContent(sensitiveFilter.filter(post.getTitle()));
        System.out.println("++++++++++++");
        System.out.println(post.getContent());
    }
    
}

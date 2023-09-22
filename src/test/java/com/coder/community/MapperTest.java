package com.coder.community;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.coder.community.dao.DiscussPostMapper;
import com.coder.community.dao.LoginTicketMapper;
import com.coder.community.dao.MessageMapper;
import com.coder.community.dao.UserMapper;
import com.coder.community.entity.DiscussPost;
import com.coder.community.entity.LoginTicket;
import com.coder.community.entity.Message;
import com.coder.community.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectUser() {
        User user = userMapper.selectByName("liubei");
        System.out.println(user);
        
        user = userMapper.selectByEmail("nowcoder13@sina.com");
        System.out.println(user);
        
        user = userMapper.selectById(101);
        System.out.println(user);
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("Tianwei_Mo");
        user.setPassword("19283746");
        user.setSalt("sdf23");
        user.setEmail("123456789@gmail.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());
        System.out.println(user);
        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void testUpdate() {
        int rows = userMapper.updateStatus(151, 1);
        System.out.println(rows);

        rows = userMapper.updateHeader(151, "http://www.nowcoder.com/104.png");
        System.out.println(rows);

        rows = userMapper.updatePassword(151, "123456789");
        System.out.println(rows);
    }

    @Autowired
    private DiscussPostMapper discussPostMapper;
    
    @Test
    public void testSelectDiscussPosts() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149, 0, 10, 0);
        for(DiscussPost post : list) {
            System.out.println(post);
        }

        int rows = discussPostMapper.selectDiscussPostRows(149
        );
        System.out.println(rows);
    }

    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Test
    public void testInsertLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(149);
        loginTicket.setTicket("123");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 6000));

        int insert = loginTicketMapper.insertLoginTicket(loginTicket);
        System.out.println(insert);
    }

    @Test
    public void testSelectByTicket() {
        LoginTicket test = loginTicketMapper.selectByTicket("123");
        System.out.println((test));
    }

    @Test
    public void testUpdateStatus() {
        int change = loginTicketMapper.updateStatus("123", 1);
        System.out.println(change);
        LoginTicket test = loginTicketMapper.selectByTicket("123");
        System.out.println((test));
    }

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testMessageMapper() {
        List<Message> list = messageMapper.selectConversations(111, 0, 20);
        for (Message m : list) {
            System.out.println(m);
        }
        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        list = messageMapper.selectLetters("111_112", 0, 10);
        for (Message m : list) {
            System.out.println(m);
        }

        count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);
        
        count = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(count);
    }
}

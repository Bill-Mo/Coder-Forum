package com.coder.community.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.coder.community.entity.Comment;
import com.coder.community.entity.DiscussPost;
import com.coder.community.entity.Event;
import com.coder.community.event.EventProducer;
import com.coder.community.service.CommentService;
import com.coder.community.service.DiscussPostService;
import com.coder.community.util.CommunityConstant;
import com.coder.community.util.HostHolder;
import com.coder.community.util.RedisKeyUtil;

@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant{

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    DiscussPostService discussPostService;

    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping(path = "/add/{disscussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("disscussPostId") int discussPostId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        // Activate event
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId", discussPostId);

        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());   
        }
        eventProducer.fireEvent(event);

        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            // Publish an event
            event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(comment.getUserId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(discussPostId);

            eventProducer.fireEvent(event);

            // Calculate score
            String redisKey = RedisKeyUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(redisKey, discussPostId);
        }
        
        return "redirect:/discuss/detail/" + discussPostId;
    }
}

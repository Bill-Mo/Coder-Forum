package com.coder.community.quartz;

import com.coder.community.entity.DiscussPost;
import com.coder.community.service.DiscussPostService;
import com.coder.community.service.ElasticsearchService;
import com.coder.community.service.LikeService;
import com.coder.community.util.CommunityConstant;
import com.coder.community.util.RedisKeyUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostScoreRefreshJob implements Job, CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(PostScoreRefreshJob.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    // Coder epoch
    private static final Date epoch;

    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-06-27 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("Initialization failed!", e);
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String redisKey = RedisKeyUtil.getPostScoreKey();
        BoundSetOperations operations = redisTemplate.boundSetOps(redisKey);

        if (operations.size() == 0) {
            logger.info("[Mission Cancelled] No post score need to be calculate!");
            return;
        }

        logger.info("[Mission Start] Calculating post. Post number: " + operations.size());
        while (operations.size() > 0) {
            this.refresh((Integer) operations.pop());
        }
        logger.info("[Mission Complete] Post score calculation finished!");
    }

    private void refresh(int postId) {
        DiscussPost post = discussPostService.findDiscussPostById(postId);

        if (post == null) {
            logger.error("Post not found. id = " + postId);
            return;
        }

        boolean wonderful = post.getStatus() == 1;
        int commentCount = post.getCommentCount();
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, postId);

        // Weight
        double w = (wonderful ? 75 : 0) + commentCount * 10 + likeCount * 2;
        // Score = weight + date
        double score = Math.log10(Math.max(w, 1))
                + (post.getCreateTime().getTime() - epoch.getTime()) / (1000 * 3600 * 24);
        // Update score for the post
        discussPostService.updateScore(postId, score);
        // Update score to es
        post.setScore(score);
        elasticsearchService.saveDiscussPost(post);
    }

}

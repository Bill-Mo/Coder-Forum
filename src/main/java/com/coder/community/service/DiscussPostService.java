package com.coder.community.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.elasticsearch.index.shard.IllegalIndexShardStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.coder.community.dao.DiscussPostMapper;
import com.coder.community.entity.DiscussPost;
import com.coder.community.util.SensitiveFilter;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

@Service
public class DiscussPostService {

    private static final Logger logger = LoggerFactory.getLogger(DiscussPostService.class);
    
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Value("${caffeine.posts.max-size}")
    private int maxSize;
    
    @Value("${caffeine.posts.expire-seconds}")
    private int expireSeconds;

    private LoadingCache<String, List<DiscussPost>> postListCache;

    private LoadingCache<Integer, Integer> postRowCache;

    @PostConstruct
    public void init() {
        postListCache = Caffeine.newBuilder()
                    .maximumSize(maxSize)
                    .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                    .build(new CacheLoader<String ,List<DiscussPost>>() {

                        @Override
                        public @Nullable List<DiscussPost> load(@NonNull String key) throws Exception {
                            if (key == null || key.length() == 0) {
                                throw new IllegalArgumentException("Invalid argument!");
                            }
                            
                            String[] params = key.split(":");
                            if (params == null || params.length != 2) {
                                throw new IllegalArgumentException("Invalid argument!");
                            }

                            int offset = Integer.parseInt(params[0]);
                            int limit = Integer.parseInt(params[1]);

                            return discussPostMapper.selectDiscussPosts(0, offset, limit, 1);
                        }
                    });

        postRowCache = Caffeine.newBuilder()
                    .maximumSize(maxSize)
                    .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                    .build(new CacheLoader<Integer, Integer>() {

                        @Override
                        public @Nullable Integer load(@NonNull Integer key) throws Exception {
                            logger.debug("Load post row from DB");
                            return discussPostMapper.selectDiscussPostRows(key);
                        }
                    });
    }

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit, int orderMode) {
        if (userId == 0 && orderMode == 1) {
            return postListCache.get(offset + ":" + limit);
        }

        logger.debug("Load post list from DB");
        return discussPostMapper.selectDiscussPosts(userId, offset, limit, orderMode);
    }

    public int findDiscussPostRows(int userId) {
        if (userId == 0) {
            return postRowCache.get(0);
        }

        logger.debug("Load post rows from DB");
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be blank!");
        }

        // HTML esacpe
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));

        // Filter sensitive information
        System.out.println(post.getTitle().equals("喝酒开票"));
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insertDiscussPost(post);
    }

    public DiscussPost findDiscussPostById(int id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

    public int updateCommentCount(int id, int commentCount) {
        return discussPostMapper.updateCommentCount(id, commentCount);
    }

    public int updateType(int id, int type) {
        return discussPostMapper.updateType(id, type);
    }

    public int updateStatus(int id, int status) {
        return discussPostMapper.updateStatus(id, status);
    }

    public int updateScore(int id, double score) {
        return discussPostMapper.updateScore(id, score);
    }
}

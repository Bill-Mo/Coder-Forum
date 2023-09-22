package com.coder.community.dao.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.coder.community.entity.DiscussPost;

@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer>{
    
}

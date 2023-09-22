package com.coder.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.coder.community.entity.Comment;

@Mapper
public interface CommentMapper {
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);

    Comment selectCommentById(int id);
}

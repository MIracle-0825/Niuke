package com.ding.niuke.mapper;

import com.ding.niuke.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    //有多个参数时，要用@param标识！
    List<Comment> selectCommentsByEntity(@Param("entityType") int entityType, @Param("entityId")int entityId, @Param("offset")int offset, @Param("limit")int limit);

    int selectCountByEntity(@Param("entityType")int entityType,@Param("entityId")int entityId);

    int insertComment(Comment comment);
}

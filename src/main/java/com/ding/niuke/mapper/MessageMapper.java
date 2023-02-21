package com.ding.niuke.mapper;

import com.ding.niuke.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    List<Message> selectConversations(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    int selectConversationCount(int userId);

    List<Message> selectLetters(@Param("conversationId") String conversationId,@Param("offset") int offset,@Param("limit") int limit);

    int selectLetterCount(String conversationId);

    int selectLetterUnreadCount(@Param("userId") int userId,@Param("conversationId") String conversationId);

    int insertMessage(Message message);

    int updateStatus(@Param("ids") List<Integer> ids, @Param("status") int status);

    Message selectLatestNotice(@Param("userId")int userId,@Param("topic")String topic);

    int selectNoticeCount(@Param("userId")int userId,@Param("topic")String topic);

    int selectNoticeUnreadCount(@Param("userId")int userId,@Param("topic")String topic);

    List<Message> selectNotices(int userId, String topic, int offset, int limit);
}

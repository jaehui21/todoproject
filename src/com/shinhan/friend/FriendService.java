package com.shinhan.friend;

import java.util.List;

import com.shinhan.todo.ToDoDTO;

public class FriendService {
    FriendDAO friendDAO = new FriendDAO();

    public List<FriendDTO> getAllFriends(int userId) {
        return friendDAO.selectAllFriends(userId);
    }

    public int addFriend(FriendDTO friend) {
        return friendDAO.insertFriend(friend);
    }

    public int updateFriendNickname(int userId, int friendId, String nickname) {
        return friendDAO.updateNickname(userId, friendId, nickname);
    }

    public int removeFriend(int userId, int friendId) {
        return friendDAO.deleteFriend(userId, friendId);
    }

    public List<ToDoDTO> getFriendsTodo(int friendUserId) {
        return friendDAO.selectFriendsTodo(friendUserId);
    }
}
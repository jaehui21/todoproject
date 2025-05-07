package com.shinhan.friend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shinhan.main.DBUtil;
import com.shinhan.todo.ToDoDAO;
import com.shinhan.todo.ToDoDTO;

public class FriendDAO {
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    int resultCount;

    // SQL
    static final String SELECT_ALL = "SELECT * FROM friends WHERE user_id = ?";
    static final String INSERT = "INSERT INTO friends (friend_id, user_id, friend_user_id, nickname) VALUES (friend_seq.NEXTVAL, ?, ?, ?)";
    static final String UPDATE_NICKNAME = "UPDATE friends SET nickname = ? WHERE friend_id = ? AND user_id = ?";
    static final String DELETE = "DELETE FROM friends WHERE friend_id = ? AND user_id = ?";
    static final String SELECT_FRIEND_TODO = "SELECT t.*, c.category_name, p.priority_level "
            + "FROM todo t LEFT JOIN category c ON t.category_id = c.category_id "
            + "LEFT JOIN priority p ON t.priority_id = p.priority_id "
            + "WHERE TRUNC(todo_date) = TRUNC(SYSDATE) AND t.user_id = ?"
            + "ORDER BY t.is_check ASC, t.todo_date ASC";

    public List<FriendDTO> selectAllFriends(int userId) {
        List<FriendDTO> list = new ArrayList<>();
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(SELECT_ALL);
            pst.setInt(1, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
                FriendDTO friend = new FriendDTO();
                friend.setFriend_id(rs.getInt("friend_id"));
                friend.setUser_id(rs.getInt("user_id"));
                friend.setFriend_user_id(rs.getInt("friend_user_id"));
                friend.setNickname(rs.getString("nickname"));
                list.add(friend);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, rs);
        }
        return list;
    }

    public int insertFriend(FriendDTO friend) {
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(INSERT);
            pst.setInt(1, friend.getUser_id());
            pst.setInt(2, friend.getFriend_user_id());
            pst.setString(3, friend.getNickname());
            resultCount = pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("이미 친구로 등록된 사용자이거나 잘못된 ID입니다.");
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return resultCount;
    }

    public int updateNickname(int userId, int friendId, String nickname) {
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(UPDATE_NICKNAME);
            pst.setString(1, nickname);
            pst.setInt(2, friendId);
            pst.setInt(3, userId);
            resultCount = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return resultCount;
    }

    public int deleteFriend(int userId, int friendId) {
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(DELETE);
            pst.setInt(1, friendId);
            pst.setInt(2, userId);
            resultCount = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return resultCount;
    }
    
    public List<ToDoDTO> selectFriendsTodo(int friendUserId) {
        List<ToDoDTO> list = new ArrayList<>();
        conn = DBUtil.getConnection();

        try {
            pst = conn.prepareStatement(SELECT_FRIEND_TODO);
            pst.setInt(1, friendUserId);
            rs = pst.executeQuery();

            while (rs.next()) {
                ToDoDTO todo = ToDoDAO.makeToDo(rs); // 기존에 있는 ResultSet → DTO 변환 메서드
                list.add(todo);
            }
        } catch (SQLException e) {
            // 예외가 발생해도 아무 메시지도 출력하지 않음 (조용히 무시)
        } finally {
            DBUtil.dbDisconnect(conn, pst, rs);
        }
        return list;
    }
}

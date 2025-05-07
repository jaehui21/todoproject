package com.shinhan.user;

import java.sql.*;

import com.shinhan.main.DBUtil;

public class UserDAO {
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    int resultCount;
    UserDTO user = null;

    // SQL 문 상수 정의
    static final String INSERT_USER = "INSERT INTO users (name, email, password, nickname) VALUES (?, ?, ?, ?)";
    static final String LOGIN = "SELECT * FROM users WHERE email = ? AND password = ?";
    static final String UPDATE_NICKNAME = "UPDATE users SET nickname = ? WHERE user_id = ?";
    static final String DELETE_NICKNAME = "UPDATE users SET nickname = NULL WHERE user_id = ?";
    static final String FIND_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";

    // 회원 등록
    public int insertUser(UserDTO user) {
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(INSERT_USER);
            pst.setString(1, user.getName());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getPassword());
            pst.setString(4, user.getNickname());
            resultCount = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return resultCount;
    }

    // 로그인
    public UserDTO login(String email, String password) {
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(LOGIN);
            pst.setString(1, email);
            pst.setString(2, password);
            rs = pst.executeQuery();
            if (rs.next()) {
                user = UserDTO.builder()
                        .user_id(rs.getInt("user_id"))
                        .name(rs.getString("name"))
                        .email(rs.getString("email"))
                        .password(rs.getString("password"))
                        .nickname(rs.getString("nickname"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, rs);
        }
        return user;
    }

    // 닉네임 수정
    public int updateNickname(int userId, String nickname) {
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(UPDATE_NICKNAME);
            pst.setString(1, nickname);
            pst.setInt(2, userId);
            resultCount = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return resultCount;
    }

    // 닉네임 삭제
    public int deleteNickname(int userId) {
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(DELETE_NICKNAME);
            pst.setInt(1, userId);
            resultCount = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return resultCount;
    }
    
    // 친구 기능에서 사용하는 이메일로 사용자 찾기
    public UserDTO findUserByEmail(String email) {
        conn = DBUtil.getConnection();

        try {
            pst = conn.prepareStatement(FIND_USER_BY_EMAIL);
            pst.setString(1, email);
            rs = pst.executeQuery();

            if (rs.next()) {
                user = new UserDTO();
                user.setUser_id(rs.getInt("user_id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setNickname(rs.getString("nickname"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, rs);
        }
        return user;
    }
}

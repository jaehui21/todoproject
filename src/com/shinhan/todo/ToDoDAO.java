package com.shinhan.todo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.shinhan.main.DBUtil;

public class ToDoDAO {
    Connection conn;
    Statement st;
    PreparedStatement pst;
    ResultSet rs;
    int resultCount;

    static final String SELECT_ALL_TODAY = "SELECT t.*, c.category_name, p.priority_level "
            + "FROM todo t LEFT JOIN category c ON t.category_id = c.category_id "
            + "LEFT JOIN priority p ON t.priority_id = p.priority_id "
            + "WHERE TRUNC(todo_date) = TRUNC(SYSDATE) AND t.user_id = ?"
            + "ORDER BY t.is_check ASC, t.todo_date ASC";
    static final String SELECT_ALL = "SELECT t.tno, t.content, t.todo_date, t.is_check, "
    	  + "  t.user_id, t.category_id, t.priority_id, c.category_name, p.priority_level "
    	  + "FROM todo t LEFT JOIN category c ON t.category_id = c.category_id "
    	  + "LEFT JOIN priority p ON t.priority_id = p.priority_id WHERE t.user_id = ? "
    	  + "ORDER BY t.is_check ASC, t.todo_date ASC";
    static final String SELECT_BY_DATE = "SELECT t.*, c.category_name, p.priority_level "
            + "FROM todo t LEFT JOIN category c ON t.category_id = c.category_id "
            + "LEFT JOIN priority p ON t.priority_id = p.priority_id "
            + "WHERE todo_date = ? AND t.user_id = ? ORDER BY t.is_check ASC";
    static final String INSERT_TODO = "INSERT INTO todo (content, todo_date, is_check, user_id, category_id, priority_id) VALUES (?, ?, ?, ?, ?, ?)";
    static final String UPDATE_TODO = "UPDATE todo SET content = ?, todo_date = ?, is_check = ?, category_id = ?, priority_id = ? "
            + "WHERE tno = ? AND user_id = ?";
    static final String DELETE_TODO = "DELETE FROM todo WHERE tno = ?";
    static final String UPDATE_CHECK = "UPDATE todo SET is_check = ? WHERE tno = ?";
    static final String SELECT_BY_TNO = "SELECT t.*, c.category_name, p.priority_level "
            + "FROM todo t LEFT JOIN category c ON t.category_id = c.category_id "
            + "LEFT JOIN priority p ON t.priority_id = p.priority_id "
            + "WHERE t.user_id = ? AND t.tno = ?";
    
    public static ToDoDTO makeToDo(ResultSet rs) throws SQLException {
        return ToDoDTO.builder()
                .tno(rs.getInt("tno"))
                .content(rs.getString("content"))
                .todo_date(rs.getDate("todo_date"))
                .is_check(rs.getBoolean("is_check"))
                .user_id(rs.getInt("user_id"))
                .category_id(rs.getInt("category_id"))
                .priority_id(rs.getInt("priority_id"))
                .category_name(rs.getString("category_name"))
                .priority_level(rs.getString("priority_level"))
                .build();
    }

    // 사용자의 오늘 할 일 조회
    public List<ToDoDTO> selectAllToday(int userId) {
        List<ToDoDTO> todolist = new ArrayList<>();
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(SELECT_ALL_TODAY);
            pst.setInt(1, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
                ToDoDTO todo = makeToDo(rs);
                todolist.add(todo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, rs);
        }
        return todolist;
    }
    
    // 사용자의 모든 할 일 조회
    public List<ToDoDTO> selectAll(int userId) {
        List<ToDoDTO> todolist = new ArrayList<>();
        conn = DBUtil.getConnection();

        try {
            pst = conn.prepareStatement(SELECT_ALL);
            pst.setInt(1, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
                ToDoDTO todo = makeToDo(rs);
                todolist.add(todo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, rs);
        }
        return todolist;
    }

    // 사용자의 특정 할 일 조회
	public ToDoDTO selectByTno(int userId, int tno) {
        ToDoDTO todo = null;
        conn = DBUtil.getConnection();
        
        try {
            pst = conn.prepareStatement(SELECT_BY_TNO);
            pst.setInt(1, userId);
            pst.setInt(2, tno);
            rs = pst.executeQuery();
			if(rs.next()) {
				todo = makeToDo(rs);
			}
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, rs);
        }
        return todo;
	}

    // 사용자의 특정 날짜 할 일 조회
    public List<ToDoDTO> selectByDate(Date date, int userId) {
        List<ToDoDTO> todolist = new ArrayList<>();
        conn = DBUtil.getConnection();
        
        try {
            pst = conn.prepareStatement(SELECT_BY_DATE);
            pst.setDate(1, date);
            pst.setInt(2, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
                ToDoDTO todo = makeToDo(rs);
                todolist.add(todo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, rs);
        }
        return todolist;
    }

    // 할 일 등록
    public int insertToDo(ToDoDTO todo) {
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(INSERT_TODO);
            pst.setString(1, todo.getContent());
            pst.setDate(2, todo.getTodo_date());
            pst.setBoolean(3, todo.is_check());
            pst.setInt(4, todo.getUser_id());
            // null이면 NULL 아니면 실제 값 입력
            if (todo.getCategory_id() == null) {
                pst.setNull(5, java.sql.Types.INTEGER);
            } else {
                pst.setInt(5, todo.getCategory_id());
            }
            if (todo.getPriority_id() == null) {
                pst.setNull(6, java.sql.Types.INTEGER);
            } else {
                pst.setInt(6, todo.getPriority_id());
            }
            resultCount = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return resultCount;
    }

    // 할 일 수정
    public int updateToDo(ToDoDTO todo) {
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(UPDATE_TODO);

            pst.setString(1, todo.getContent());
            pst.setDate(2, todo.getTodo_date());
            pst.setBoolean(3, todo.is_check());

            // 0이 들어갔을 때까지 처리해야 됨..
            if (todo.getCategory_id() == null || todo.getCategory_id() == 0) {
                pst.setNull(4, java.sql.Types.INTEGER);
            } else {
                pst.setInt(4, todo.getCategory_id());
            }
            if (todo.getPriority_id() == null || todo.getPriority_id() == 0) {
                pst.setNull(5, java.sql.Types.INTEGER);
            } else {
                pst.setInt(5, todo.getPriority_id());
            }
            pst.setInt(6, todo.getTno());

            pst.setInt(7, todo.getUser_id());

            resultCount = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return resultCount;
    }


    // 할 일 삭제
    public int deleteToDo(int tno) {
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(DELETE_TODO);
            pst.setInt(1, tno);
            resultCount = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return resultCount;
    }

    // 할 일 체크/체크 해제
    public int updateCheck(int tno, boolean isCheck) {
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(UPDATE_CHECK);
            pst.setBoolean(1, isCheck);
            pst.setInt(2, tno);
            resultCount = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return resultCount;
    }
}

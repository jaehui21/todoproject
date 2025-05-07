package com.shinhan.category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.shinhan.main.DBUtil;

public class CategoryDAO {
    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;
    private int resultCount;

    // SQL 문 상수 정의
    static final String INSERT_CATEGORY = "INSERT INTO category (category_name, user_id) VALUES (?, ?)";
    static final String SELECT_ALL_CATEGORIES = "SELECT * FROM category WHERE user_id = ?";
    static final String SELECT_CATEGORY_BY_ID = "SELECT * FROM categories WHERE category_id = ? AND user_id = ?";
    static final String UPDATE_CATEGORY = "UPDATE category SET category_name = ? WHERE category_id = ? AND user_id = ?";
    static final String DELETE_CATEGORY = "DELETE FROM category WHERE category_id = ? AND user_id = ?";

    // 카테고리 추가
    public int insertCategory(CategoryDTO category) {
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(INSERT_CATEGORY);
            pst.setString(1, category.getCategory_name());
            pst.setInt(2, category.getUser_id());
            resultCount = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return resultCount;
    }

    // 전체 카테고리 조회
    public List<CategoryDTO> selectAllCategories(int userId) {
    	List<CategoryDTO> categorylist = new ArrayList<>();
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement("SELECT * FROM category WHERE user_id = ?");
            pst.setInt(1, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
                CategoryDTO category = CategoryDTO.builder()
                        .category_id(rs.getInt("category_id"))
                        .category_name(rs.getString("category_name"))
                        .user_id(rs.getInt("user_id"))
                        .build();
                categorylist.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, rs);
        }
        return categorylist;
    }
    
    // 카테고리 조회 (ID로)
    public CategoryDTO selectCategoryById(int categoryId, int userId) {
        CategoryDTO category = null;
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(SELECT_CATEGORY_BY_ID);
            pst.setInt(1, categoryId);
            pst.setInt(2, userId);
            rs = pst.executeQuery();
            if (rs.next()) {
                category = CategoryDTO.builder()
                        .category_id(rs.getInt("category_id"))
                        .category_name(rs.getString("category_name"))
                        .user_id(rs.getInt("user_id"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, rs);
        }
        return category;
    }

    // 카테고리 수정
    public int updateCategory(CategoryDTO category) {
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(UPDATE_CATEGORY);
            pst.setString(1, category.getCategory_name());
            pst.setInt(2, category.getCategory_id());
            pst.setInt(3, category.getUser_id());
            resultCount = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return resultCount;
    }

    // 카테고리 삭제
    public int deleteCategory(int categoryId, int userId) {
        conn = DBUtil.getConnection();
        try {
            pst = conn.prepareStatement(DELETE_CATEGORY);
            pst.setInt(1, categoryId);
            pst.setInt(2, userId);
            resultCount = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, pst, null);
        }
        return resultCount;
    }
    
    public static Integer findCategoryIdByName(String name, int user_id) {
        Integer id = null;
        String sql = "SELECT category_id FROM CATEGORY WHERE category_name = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, name);
            pst.setInt(2, user_id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt("category_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

}

package com.shinhan.category;

import java.util.List;

public class CategoryService {
    
    CategoryDAO categoryDAO = new CategoryDAO();

    // 사용자 ID로 전체 카테고리 조회
    public List<CategoryDTO> selectAllCategories(int userId) {
        return categoryDAO.selectAllCategories(userId);
    }
    
    // 카테고리 조회 (ID로)
    public CategoryDTO selectCategoryById(int categoryId, int userId) {
        return categoryDAO.selectCategoryById(categoryId, userId);
    }

    // 카테고리 추가
    public int insertCategory(CategoryDTO category) {
        return categoryDAO.insertCategory(category);
    }

    // 카테고리 수정
    public int updateCategory(CategoryDTO category) {
        return categoryDAO.updateCategory(category);
    }

    // 카테고리 삭제
    public int deleteCategory(int categoryId, int userId) {
        return categoryDAO.deleteCategory(categoryId, userId);
    }
}

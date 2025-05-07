package com.shinhan.category;

import java.util.List;
import java.util.Scanner;

import com.shinhan.user.LoginSession;

public class CategoryController {

    static Scanner sc = new Scanner(System.in);
    static CategoryService categoryService = new CategoryService();

    // 카테고리 전체 조회 (인덱스 포함)
    public void f_selectAllCategories() {
        if (!LoginSession.isLoggedIn()) {
            System.out.println("로그인이 필요합니다.");
            return;
        }

        int userId = LoginSession.loggedInUser.getUser_id();
        List<CategoryDTO> categoryList = categoryService.selectAllCategories(userId);
        if (categoryList.isEmpty()) {
            System.out.println("등록된 카테고리가 없습니다.");
        } else {
            for (int i = 0; i < categoryList.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, categoryList.get(i).getCategory_name());
            }
        }
    }


    // 카테고리 추가
    public void f_insertCategory() {
        if (!LoginSession.isLoggedIn()) {
            System.out.println("로그인이 필요합니다.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        int userId = LoginSession.loggedInUser.getUser_id();

        System.out.print("새 카테고리 이름 입력 >> ");
        String categoryName = sc.nextLine().trim();

        if (categoryName.isEmpty()) {
            System.out.println("카테고리 이름은 비워 둘 수 없습니다.");
            return;
        }

        CategoryDTO newCategory = new CategoryDTO();
        newCategory.setUser_id(userId);
        newCategory.setCategory_name(categoryName);

        int result = categoryService.insertCategory(newCategory);
        if (result > 0) {
            System.out.println("카테고리 추가 완료!");
        } else {
            System.out.println("카테고리 추가 실패");
        }
    }

    public void f_updateCategory() {
        if (!LoginSession.isLoggedIn()) {
            System.out.println("로그인이 필요합니다.");
            return;
        }

        int userId = LoginSession.loggedInUser.getUser_id();
        List<CategoryDTO> categoryList = categoryService.selectAllCategories(userId);
        if (categoryList.isEmpty()) {
            System.out.println("수정할 카테고리가 없습니다.");
            return;
        }

        for (int i = 0; i < categoryList.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, categoryList.get(i).getCategory_name());
        }

        System.out.print("수정할 번호 선택 >> ");
        int idx = sc.nextInt() - 1;
        sc.nextLine();

        if (idx < 0 || idx >= categoryList.size()) {
            System.out.println("잘못된 번호입니다.");
            return;
        }

        CategoryDTO selected = categoryList.get(idx);
        System.out.println("기존 카테고리: " + selected.getCategory_name());
        System.out.print("수정할 카테고리 이름 입력 >> ");
        String newName = sc.nextLine();

        if (!newName.trim().isEmpty()) {
            selected.setCategory_name(newName);
        }

        int result = categoryService.updateCategory(selected);
        if (result > 0) {
            System.out.println("카테고리 수정 완료!");
        } else {
            System.out.println("카테고리 수정 실패");
        }
    }


    // 카테고리 삭제
    public void f_deleteCategory() {
        if (!LoginSession.isLoggedIn()) {
            System.out.println("로그인이 필요합니다.");
            return;
        }

        int userId = LoginSession.loggedInUser.getUser_id();
        List<CategoryDTO> categoryList = categoryService.selectAllCategories(userId);
        if (categoryList.isEmpty()) {
            System.out.println("삭제할 카테고리가 없습니다.");
            return;
        }

        for (int i = 0; i < categoryList.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, categoryList.get(i).getCategory_name());
        }

        System.out.print("삭제할 번호 선택 >> ");
        int idx = sc.nextInt() - 1;

        if (idx < 0 || idx >= categoryList.size()) {
            System.out.println("잘못된 번호입니다.");
            return;
        }

        CategoryDTO selected = categoryList.get(idx);
        int result = categoryService.deleteCategory(selected.getCategory_id(), userId);
        if (result > 0) {
            System.out.println("카테고리 삭제 완료!");
        } else {
            System.out.println("카테고리 삭제 실패");
        }
    }
}
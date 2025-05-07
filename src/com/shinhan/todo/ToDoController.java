package com.shinhan.todo;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import com.shinhan.category.CategoryDAO;
import com.shinhan.main.DateUtil;
import com.shinhan.priority.PriorityDAO;
import com.shinhan.user.LoginSession;

public class ToDoController {

    static Scanner sc = new Scanner(System.in);
    static ToDoService todoService = new ToDoService();

    public void f_selectAllToday() {
        if (!LoginSession.isLoggedIn()) {
            System.out.println("로그인이 필요합니다.");
            return;
        }
        int userId = LoginSession.loggedInUser.getUser_id();

        java.util.Date today = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일 (E)");
        System.out.println("오늘 날짜: " + sdf.format(today));

        List<ToDoDTO> todolist = todoService.selectAllToday(userId);
        if (todolist.isEmpty()) {
            System.out.println("오늘 할 일이 없습니다.");
        } else {
            for (int i = 0; i < todolist.size(); i++) {
                ToDoDTO todo = todolist.get(i);
                System.out.printf("%d. %s\n", i + 1, todo);
            }
        }
    }

    public void f_selectByDate(Date date) {
        if (!LoginSession.isLoggedIn()) {
            System.out.println("로그인이 필요합니다.");
            return;
        }
        int userId = LoginSession.loggedInUser.getUser_id();

        SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일 (E)");
        System.out.println("검색한 날짜: " + sdf.format(date));

        List<ToDoDTO> todolist = todoService.selectByDate(date, userId);
        if (todolist.isEmpty()) {
            System.out.println(date + "에 등록된 할 일이 없습니다.");
        } else {
            for (int i = 0; i < todolist.size(); i++) {
                ToDoDTO todo = todolist.get(i);
                System.out.printf("%d. %s\n", i + 1, todo);
            }
        }
    }

    public static void f_insertToDo() {
        if (!LoginSession.isLoggedIn()) {
            System.out.println("로그인이 필요합니다.");
            return;
        }

        int user_id = LoginSession.loggedInUser.getUser_id();

        int td = todoService.insertToDo(makeToDo(user_id));
        if (td > 0) {
            System.out.println("할 일 추가 완료!");
        } else {
            System.out.println("할 일 추가 실패.");
        }
    }

    static ToDoDTO makeToDo(int user_id) {
        System.out.print("할 일 내용 입력: ");
        String content = sc.nextLine();
        if (content.equals("")) content = null;

        System.out.print("날짜[yyyy-MM-dd] 입력(엔터 시 오늘 자동 입력): >> ");
        String tdate = sc.nextLine();
        Date todo_date = null;
        if (tdate.equals("")) {
            todo_date = new java.sql.Date(System.currentTimeMillis());
        } else {
            if (!tdate.equals("0")) {
                todo_date = DateUtil.convertToSQLDate(DateUtil.convertToDate(tdate));
            }
        }

        boolean is_check = false;

        System.out.print("카테고리명(없으면 엔터) >> ");
        String cname = sc.nextLine();
        Integer category_id = null;
        if (!cname.trim().isEmpty()) {
            category_id = CategoryDAO.findCategoryIdByName(cname.trim(), user_id);
            if (category_id == null) {
                System.out.println("❗ 해당 카테고리가 없습니다. 생략 처리됩니다.");
            }
        }

        return ToDoDTO.builder()
                .content(content)
                .todo_date(todo_date)
                .is_check(is_check)
                .user_id(user_id)
                .category_id(category_id)
                .build();
    }

    public static void f_updateToDo() {
        if (!LoginSession.isLoggedIn()) {
            System.out.println("로그인이 필요합니다.");
            return;
        }

        int userId = LoginSession.loggedInUser.getUser_id();
        List<ToDoDTO> todolist = todoService.selectAll(userId);
        if (todolist.isEmpty()) {
            System.out.println("수정할 할 일이 없습니다.");
            return;
        }
        for (int i = 0; i < todolist.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, todolist.get(i));
        }
        System.out.print("수정할 번호 선택 >> ");
        int idx = sc.nextInt() - 1;
        if (idx < 0 || idx >= todolist.size()) {
            System.out.println("잘못된 번호입니다.");
            return;
        }
        ToDoDTO existingTodo = todolist.get(idx);

        System.out.println("기존 내용: " + existingTodo.getContent());
        System.out.print("새 내용 입력(없으면 엔터) >> ");
        sc.nextLine();
        String newContent = sc.nextLine();
        if (!newContent.trim().isEmpty()) {
            existingTodo.setContent(newContent);
        }

        int result = todoService.updateToDo(existingTodo);
        if (result > 0) {
            System.out.println("할 일 수정 완료!");
        } else {
            System.out.println("할 일 수정 실패");
        }
    }

    public static void f_deleteToDo() {
        if (!LoginSession.isLoggedIn()) {
            System.out.println("로그인이 필요합니다.");
            return;
        }

        int userId = LoginSession.loggedInUser.getUser_id();
        List<ToDoDTO> todolist = todoService.selectAll(userId);
        if (todolist.isEmpty()) {
            System.out.println("삭제할 할 일이 없습니다.");
            return;
        }
        for (int i = 0; i < todolist.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, todolist.get(i));
        }
        System.out.print("삭제할 번호 선택 >> ");
        int idx = sc.nextInt() - 1;
        if (idx < 0 || idx >= todolist.size()) {
            System.out.println("잘못된 번호입니다.");
            return;
        }
        int tno = todolist.get(idx).getTno();
        int td = todoService.deleteToDo(tno);
        if (td > 0) {
            System.out.println("할 일 삭제 완료!");
        } else {
            System.out.println("할 일 삭제 실패");
        }
    }

    public static void f_updateCheck() {
        if (!LoginSession.isLoggedIn()) {
            System.out.println("로그인이 필요합니다.");
            return;
        }

        int userId = LoginSession.loggedInUser.getUser_id();
        List<ToDoDTO> todolist = todoService.selectAll(userId);
        if (todolist.isEmpty()) {
            System.out.println("변경할 할 일이 없습니다.");
            return;
        }
        for (int i = 0; i < todolist.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, todolist.get(i));
        }
        System.out.print("변경할 번호 선택 >> ");
        int idx = sc.nextInt() - 1;
        if (idx < 0 || idx >= todolist.size()) {
            System.out.println("잘못된 번호입니다.");
            return;
        }
        System.out.print("체크 상태 (true/false) >> ");
        boolean isCheck = sc.nextBoolean();
        int tno = todolist.get(idx).getTno();
        int td = todoService.updateCheck(tno, isCheck);
        if (td > 0) {
            System.out.println("상태 변경 완료!");
        } else {
            System.out.println("상태 변경 실패");
        }
    }
}


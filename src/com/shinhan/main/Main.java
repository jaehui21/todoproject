package com.shinhan.main;

import java.sql.Date;
import java.util.Scanner;

import com.shinhan.category.CategoryController;
import com.shinhan.friend.FriendController;
import com.shinhan.todo.ToDoController;
import com.shinhan.user.LoginSession;
import com.shinhan.user.UserController;

import java.sql.Date;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static UserController userController = new UserController();
    static ToDoController todoController = new ToDoController();
    static CategoryController categoryController = new CategoryController();
    static FriendController friendController = new FriendController();

    public static void main(String[] args) {
        boolean run = true;

        while (run) {
            if (!LoginSession.isLoggedIn()) {
            	noMemeberDisplay();
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        userController.signUp();
                        break;
                    case 2:
                        LoginSession.loggedInUser = userController.login();
                        break;
                    case 0:
                        run = false;
                        break;
                    default:
                        System.out.println("유효한 번호를 입력해 주세요!");
                }
            } else {
                menuDisplay();
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        myToDoMenu();
                        break;
                    case 2:
                        myCategoryMenu();
                        break;
                    case 3:
                        myFriendMenu();
                        break;
                    case 4:
                        showSettingsMenu();
                        break;
                    case 5:
                        LoginSession.loggedInUser = null;
                        System.out.println("로그아웃 완료!");
                        break;
                    default:
                        System.out.println("유효한 번호를 입력해 주세요!");
                }
            }
        }

        System.out.println("===================================");
        System.out.println("          ToDo 프로그램 종료          ");
        System.out.println("===================================");

    }

	private static void myToDoMenu() {
        boolean inToDoMenu = true;

        while (inToDoMenu) {
        	todoMenuDisplay();
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    todoController.f_selectAllToday();
                    break;
                case 2:
                    System.out.print("날짜 입력(yyyy-MM-dd) >> ");
                    String dateStr = sc.next();
                    try {
                        Date date = Date.valueOf(dateStr);
                        todoController.f_selectByDate(date);
                    } catch (IllegalArgumentException e) {
                        System.out.println("날짜 형식이 올바르지 않습니다.");
                    }
                    break;
                case 3:
                    todoController.f_insertToDo();
                    break;
                case 4:
                    todoController.f_updateToDo();
                    break;
                case 5:
                    todoController.f_deleteToDo();
                    break;
                case 6:
                    todoController.f_updateCheck();
                    break;
                case 0:
                    inToDoMenu = false;
                    break;
                default:
                    System.out.println("유효한 번호를 입력해 주세요!");
            }
        }
    }

	private static void myCategoryMenu() {
        boolean inCategoryMenu = true;

        while (inCategoryMenu) {
        	categoryDisplay();
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    categoryController.f_selectAllCategories();
                    break;
                case 2:
                    categoryController.f_insertCategory();
                    break;
                case 3:
                    categoryController.f_updateCategory();
                    break;
                case 4:
                    categoryController.f_deleteCategory();
                    break;
                case 0:
                    inCategoryMenu = false;
                    break;
                default:
                    System.out.println("유효한 번호를 입력해 주세요!");
            }
        }
    }

	private static void myFriendMenu() {
        boolean inFriendMenu = true;

        while (inFriendMenu) {
        	friendDisplay();
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    friendController.f_showFriendList();
                    break;
                case 2:
                    friendController.f_addFriend();
                    break;
                case 3:
                	friendController.f_selectFriendsTodo();
                	break;
                case 4:
                    friendController.f_updateFriendNickname();
                    break;
                case 5:
                    friendController.f_deleteFriend();
                    break;
                case 0:
                    inFriendMenu = false;
                    break;
                default:
                    System.out.println("유효한 번호를 입력해 주세요!");
            }
        }
    }

	private static void showSettingsMenu() {
        boolean inSettings = true;

        while (inSettings) {
        	settingDisplay();
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    userController.f_showUserInfo();
                    break;
                case 2:
                    userController.f_updateNickname(LoginSession.loggedInUser.getUser_id());
                    break;
                case 3:
                    userController.f_deleteNickname(LoginSession.loggedInUser.getUser_id());
                    break;
                case 0:
                    inSettings = false;
                    break;
                default:
                    System.out.println("유효한 번호를 입력해 주세요!");
            }
        }
    }
    
    private static void noMemeberDisplay() {
    	System.out.println();
        System.out.println("====== [ 현재 상태: 비회원 ] ======");
        System.out.println("   1. 회원가입 2. 로그인 0. 종료    ");
        System.out.println("===============================");
        System.out.print("선택 >> ");
	}

    private static void menuDisplay() {
    	System.out.println();
        System.out.println("===== [ " + LoginSession.loggedInUser.getName() + " 님의 to-do APP! ] =====");
        System.out.println("1. 나의 할 일 2. 나의 카테고리 3. 나의 친구");
        System.out.println("4. 나의 설정  5. 로그아웃");
        System.out.println("====================================");
        System.out.print("선택 >> ");
    }
    
    private static void todoMenuDisplay() {
    	System.out.println();
        System.out.println("================== [ 나의 할 일 ] ==================");
        System.out.println("1. 오늘의 할 일 조회 2. 날짜별 할 일 조회 3. 할 일 추가");
        System.out.println("4. 할 일 수정 5. 할 일 삭제 6. 체크 상태 변경 0. 이전 페이지");
        System.out.println("==================================================");
        System.out.print("선택 >> ");
	}
    
    private static void categoryDisplay() {
    	System.out.println();
        System.out.println("=============== [ 나의 카테고리 ] ==============");
        System.out.println("1. 전체 카테고리 조회 2. 카테고리 추가 3. 카테고리 수정");
        System.out.println("4. 카테고리 삭제     0. 이전 페이지");
        System.out.println("=============================================");
        System.out.print("선택 >> ");
	}
    
    private static void friendDisplay() {
    	System.out.println();
        System.out.println("============== [ 나의 친구 ] ==============");
        System.out.println("1. 친구 목록 확인  2. 친구 추가 3. 친구 투두 조회");
        System.out.println("4. 친구 닉네임 수정 5. 친구 삭제 0. 이전 페이지");
        System.out.println("=========================================");
        System.out.print("선택 >> ");
	}
    
    private static void settingDisplay() {
    	System.out.println();
        System.out.println("======== [ 나의 설정 ] =======");
        System.out.println("1. 사용자 정보 조회 2. 닉네임 수정");
        System.out.println("3. 닉네임 삭제     0. 이전 페이지");
        System.out.println("============================");
        System.out.print("선택 >> ");
	}
}
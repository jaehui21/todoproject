package com.shinhan.user;

import java.util.Scanner;

public class UserController {

    static Scanner sc = new Scanner(System.in);
    static UserService userService = new UserService();

    public void signUp() {
        System.out.print("이름 >> ");
        String name = sc.next();

        System.out.print("이메일(로그인 ID) >> ");
        String email = sc.next();

        System.out.print("비밀번호 >> ");
        String password = sc.next();

        System.out.print("닉네임(Enter 시 생략) >> ");
        sc.nextLine();
        String nickname = sc.nextLine();
        if (nickname.isBlank()) nickname = null;

        UserDTO user = UserDTO.builder()
                .name(name)
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();

        int result = userService.signUp(user);
        System.out.println(result > 0 ? "회원 가입 성공!" : "회원 가입 실패");
    }

    public UserDTO login() {
        System.out.print("이메일 >> ");
        String email = sc.next();

        System.out.print("비밀번호 >> ");
        String password = sc.next();

        UserDTO user = userService.login(email, password);
        if (user != null) {
            System.out.println("로그인 성공!");
            return user;
        } else {
            System.out.println("로그인 실패..");
            return null;
        }
    }

    public void logout() {
        if (LoginSession.isLoggedIn()) {
            System.out.println(LoginSession.loggedInUser.getName() + "님이 로그아웃했습니다.");
            LoginSession.loggedInUser = null;
        } else {
            System.out.println("현재 로그인된 사용자가 없습니다.");
        }
    }
    
    public void f_updateNickname(int userId) {
        System.out.print("변경할 닉네임 >> ");
        String nickname = sc.next();

        int result = userService.updateNickname(userId, nickname);
        if (result > 0) {
            // 닉네임 변경 후 최신 정보로 다시 로그인하여 세션 갱신
            UserDTO updatedUser = userService.login(LoginSession.loggedInUser.getEmail(), LoginSession.loggedInUser.getPassword());
            if (updatedUser != null) {
                LoginSession.loggedInUser = updatedUser;
                System.out.println("닉네임 변경 완료!");
            } else {
                System.out.println("변경 후 사용자 정보 갱신 실패");
            }
        } else {
            System.out.println("닉네임 변경 실패");
        }
    }

    public void f_deleteNickname(int userId) {
        int result = userService.deleteNickname(userId);
        if (result > 0) {
            // 닉네임 삭제 후 사용자 정보 다시 불러오기
            UserDTO updatedUser = userService.login(LoginSession.loggedInUser.getEmail(), LoginSession.loggedInUser.getPassword());
            if (updatedUser != null) {
            	LoginSession.loggedInUser = updatedUser;
                System.out.println("닉네임 삭제 완료!");
            } else {
                System.out.println("삭제 후 사용자 정보 갱신 실패");
            }
        } else {
            System.out.println("닉네임 삭제 실패");
        }
    }
    
	public void f_showUserInfo() {
		System.out.println();
        System.out.println("======= [ 사용자 정보 ] =======");
        System.out.println("이메일: " + LoginSession.loggedInUser.getEmail());
        System.out.println("이름: " + LoginSession.loggedInUser.getName());
        String nickname = LoginSession.loggedInUser.getNickname();
        if (nickname == null || nickname.trim().isEmpty()) {
            System.out.println("닉네임: 설정되지 않음");
        } else {
            System.out.println("닉네임: " + nickname);
        }
        System.out.println("=============================");
    }
}
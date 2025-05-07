package com.shinhan.friend;

import java.util.List;
import java.util.Scanner;

import com.shinhan.todo.ToDoDTO;
import com.shinhan.user.LoginSession;
import com.shinhan.user.UserDAO;
import com.shinhan.user.UserDTO;

public class FriendController {
    static Scanner sc = new Scanner(System.in);
    FriendService friendService = new FriendService();

    public void f_showFriendList() {
        int userId = LoginSession.loggedInUser.getUser_id();
        List<FriendDTO> friends = friendService.getAllFriends(userId);

        if (friends.isEmpty()) {
            System.out.println("등록된 친구가 없습니다.");
        } else {
            System.out.println("\n[ 친구 목록 ]");
            for (int i = 0; i < friends.size(); i++) {
                FriendDTO friend = friends.get(i);
                System.out.printf("%d. %s%n",
                        i + 1, (friend.getNickname() != null ? friend.getNickname() : "없음"));
            }
        }
    }

    public void f_addFriend() {
        int userId = LoginSession.loggedInUser.getUser_id();

        System.out.print("추가할 친구의 이메일 입력 >> ");
        String email = sc.nextLine();

        UserDAO userDAO = new UserDAO();
        UserDTO friendUser = userDAO.findUserByEmail(email);

        if (friendUser == null) {
            System.out.println("해당 이메일로 등록된 사용자가 없습니다.");
            return;
        }

        if (friendUser.getUser_id() == userId) {
            System.out.println("자기 자신은 친구로 추가할 수 없습니다.");
            return;
        }

        System.out.print("친구 닉네임 입력 >> ");
        String nickname = sc.nextLine();
        if (nickname.isBlank()) nickname = null;

        FriendDTO friend = new FriendDTO();
        friend.setUser_id(userId);
        friend.setFriend_user_id(friendUser.getUser_id());
        friend.setNickname(nickname);

        int result = friendService.addFriend(friend);
        if (result > 0) {
            System.out.println("친구 추가 완료!");
        } else {
            System.out.println("친구 추가 실패 (이미 친구로 등록되었을 수도 있음)");
        }
    }

    public void f_updateFriendNickname() {
        int userId = LoginSession.loggedInUser.getUser_id();
        List<FriendDTO> friends = friendService.getAllFriends(userId);

        if (friends.isEmpty()) {
            System.out.println("등록된 친구가 없습니다.");
            return;
        }

        System.out.println("\n[ 친구 목록 ]");
        for (int i = 0; i < friends.size(); i++) {
            FriendDTO friend = friends.get(i);
            System.out.printf("%d. %s%n",
                    i + 1, (friend.getNickname() != null ? friend.getNickname() : "없음"));
        }

        System.out.print("수정할 친구 번호 >> ");
        int index = Integer.parseInt(sc.nextLine());

        if (index < 1 || index > friends.size()) {
            System.out.println("유효하지 않은 번호입니다.");
            return;
        }

        FriendDTO selected = friends.get(index - 1);

        System.out.print("새 닉네임 입력 >> ");
        String nickname = sc.nextLine();

        int result = friendService.updateFriendNickname(userId, selected.getFriend_id(), nickname);
        if (result > 0) {
            System.out.println("닉네임 수정 완료!");
        } else {
            System.out.println("닉네임 수정 실패");
        }
    }

    public void f_deleteFriend() {
        int userId = LoginSession.loggedInUser.getUser_id();
        List<FriendDTO> friends = friendService.getAllFriends(userId);

        if (friends.isEmpty()) {
            System.out.println("등록된 친구가 없습니다.");
            return;
        }

        System.out.println("\n[ 친구 목록 ]");
        for (int i = 0; i < friends.size(); i++) {
            FriendDTO friend = friends.get(i);
            System.out.printf("%d. %s%n",
                    i + 1, (friend.getNickname() != null ? friend.getNickname() : "없음"));
        }

        System.out.print("삭제할 친구 번호 >> ");
        int index = Integer.parseInt(sc.nextLine());

        if (index < 1 || index > friends.size()) {
            System.out.println("유효하지 않은 번호입니다.");
            return;
        }

        FriendDTO selected = friends.get(index - 1);

        int result = friendService.removeFriend(userId, selected.getFriend_id());
        if (result > 0) {
            System.out.println("친구 삭제 완료!");
        } else {
            System.out.println("삭제 실패");
        }
    }


    public void f_selectFriendsTodo() {
        int userId = LoginSession.loggedInUser.getUser_id();
        List<FriendDTO> friends = friendService.getAllFriends(userId);

        if (friends.isEmpty()) {
            System.out.println("등록된 친구가 없습니다.");
            return;
        }

        System.out.println("\n[ 친구 목록 ]");
        for (int i = 0; i < friends.size(); i++) {
            FriendDTO f = friends.get(i);
            System.out.printf("%d. %s%n", i + 1, (f.getNickname() != null ? f.getNickname() : "없음"));
        }

        System.out.print("\n조회할 친구 번호 입력 >> ");
        int index = Integer.parseInt(sc.nextLine());

        if (index < 1 || index > friends.size()) {
            System.out.println("유효하지 않은 번호입니다.");
            return;
        }

        FriendDTO selected = friends.get(index - 1);
        List<ToDoDTO> todos = friendService.getFriendsTodo(selected.getFriend_user_id());

        System.out.println("\n[ 친구의 오늘 할 일 목록 ]");
        if (todos.isEmpty()) {
            System.out.println("오늘 등록된 할 일이 없습니다.");
        } else {
            for (int i = 0; i < todos.size(); i++) {
                ToDoDTO todo = todos.get(i);
                System.out.printf("%d. %s\n", i + 1, todo);
                }
        }
    }

}
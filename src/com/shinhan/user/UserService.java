package com.shinhan.user;

public class UserService {

    UserDAO usersDAO = new UserDAO();

    public int signUp(UserDTO user) {
        return usersDAO.insertUser(user);
    }

    public UserDTO login(String email, String password) {
        return usersDAO.login(email, password);
    }

    public int updateNickname(int userId, String nickname) {
        return usersDAO.updateNickname(userId, nickname);
    }

    public int deleteNickname(int userId) {
        return usersDAO.deleteNickname(userId);
    }
}
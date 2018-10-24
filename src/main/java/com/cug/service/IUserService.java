package com.cug.service;

import com.cug.common.ServiceResponse;
import com.cug.pojo.User;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/9/17 0017.
 */
public interface IUserService {
    ServiceResponse<User> login(String username, String password);

    ServiceResponse<String> register(User user);

    ServiceResponse<String> checkValid(String str, String type);

    ServiceResponse<String> forgetGetQuestion(String username);

    ServiceResponse<String> checkAnswer(String username, String question, String answer);

    ServiceResponse<String> forgetResetPassword(String username, String password, String forgetToken);

    ServiceResponse<String> resetPassword(String oldPassword, String newPassword,User user);

    ServiceResponse<User> updateUserInfo(User user);

    ServiceResponse<User> getInformation(Integer id);

    ServiceResponse checkAdminRole(User user);

}

package com.cug.controller.manager;

import com.cug.common.Const;
import com.cug.common.ServiceResponse;
import com.cug.pojo.User;
import com.cug.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/9/21 0021.
 */
@Controller
@RequestMapping("/manage/user/")
public class UserManagerController {

    @Autowired
    private IUserService iUserService;

    /**
     * 管理员登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "admin_login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> adminLogin(String username, String password, HttpSession session){
        ServiceResponse response=iUserService.login(username,password);
        if(response.isSuccess()){
            User user= (User) response.getData();
            return iUserService.checkAdminRole(user);
        }
        return response;
    }
}

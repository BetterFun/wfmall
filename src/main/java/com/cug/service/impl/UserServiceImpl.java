package com.cug.service.impl;

import com.cug.common.Const;
import com.cug.common.MD5Util;
import com.cug.common.ServiceResponse;
import com.cug.common.TokenCache;
import com.cug.dao.UserMapper;
import com.cug.pojo.User;
import com.cug.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.util.Password;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Created by Administrator on 2018/9/17 0017.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServiceResponse<User> login(String username, String password) {
        int resultCount=userMapper.checkUsername(username);
        if(resultCount == 0){
            return ServiceResponse.createByErrorMessage("用户名不存在");
        }

        //密码登录MD5
        String MD5password=MD5Util.MD5EncodeUtf8(password);

        User user=userMapper.selectLogin(username,MD5password);
        if(user==null){
            return ServiceResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createBySuccess("登陆成功",user);
    }

    /**
     *用户注册
     * @param user
     * @return
     */
    @Override
    public ServiceResponse<String> register(User user) {
       /* int resultCount=userMapper.checkUsername(user.getUsername());
        if(resultCount > 0){
            return ServiceResponse.createByErrorMessage("用户名已存在");
        }

        resultCount=userMapper.checkEmail(user.getEmail());
        if(resultCount > 0){
            return ServiceResponse.createByErrorMessage("email存在");
        }*/

       ServiceResponse<String> validResponse=this.checkValid(user.getUsername(),Const.USERNAME);
       if(!validResponse.isSuccess()){
           return validResponse;
       }

       validResponse=this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount=userMapper.insert(user);
        if(resultCount==0){
           return ServiceResponse.createByErrorMessage("注册失败");
        }
        return ServiceResponse.createBySuccessMessage("注册成功");
    }

    /**
     * 重复性校验（用户名和邮箱）
     * @param str
     * @param type
     * @return
     */
    @Override
    public ServiceResponse<String> checkValid(String str, String type) {
        if(StringUtils.isNotBlank(type)){
            if(Const.USERNAME.equals(type)){
                int resultCount=userMapper.checkUsername(str);
                if(resultCount > 0){
                    return ServiceResponse.createByErrorMessage("用户名已存在");
                }
            }

            if(Const.EMAIL.equals(type)){
                int resultCount=userMapper.checkEmail(str);
                if(resultCount > 0){
                    return ServiceResponse.createByErrorMessage("email存在");
                }
            }
        }else {
            return ServiceResponse.createByErrorMessage("参数错误");
        }
        return ServiceResponse.createBySuccessMessage("校验成功");
    }

    /**
     * 忘记密码时获取问题
     * @param username
     * @return
     */
    @Override
    public ServiceResponse<String> forgetGetQuestion(String username) {
        ServiceResponse<String> validResponse=this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            return ServiceResponse.createByErrorMessage("用户不存在");
        }

        String question=userMapper.getQuestionByUsername(username);
        if(!StringUtils.isNotBlank(question)){
            ServiceResponse.createByErrorMessage("该问题不存在");
        }
        return ServiceResponse.createBySuccess(question);
    }

    /**
     * 校验答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @Override
    public ServiceResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount=userMapper.checkAnswer(username, question,answer);
        if(resultCount > 0){
            String forgetToken= UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PRFIX+username,forgetToken);
            return ServiceResponse.createBySuccess(forgetToken);
        }
        return ServiceResponse.createByErrorMessage("问题答案错误");
    }

    /**
     * 忘记密码时重置密码
     * @param username
     * @param newPassword
     * @param forgetToken
     * @return
     */
    @Override
    public ServiceResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
        if(StringUtils.isBlank(forgetToken)){
            return ServiceResponse.createByErrorMessage("参数错误，token为空");
        }

        ServiceResponse<String> validResponse=this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            return ServiceResponse.createByErrorMessage("用户不存在");
        }

        String token=TokenCache.getKey(TokenCache.TOKEN_PRFIX+username);
        if(StringUtils.equals(forgetToken,token)){
            String MD5password=MD5Util.MD5EncodeUtf8(newPassword);
            int resultcount=userMapper.resetPasswordByUsername(username,MD5password);
            if(resultcount>0){
                return ServiceResponse.createBySuccessMessage("修改密码成功");
            }
        }else {
            return ServiceResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
        }
        return ServiceResponse.createByErrorMessage("修改密码失败");
    }

    /**
     * 用户登录时重置密码
     * @param oldPassword
     * @param newPassword
     * @param user
     * @return
     */
    @Override
    public ServiceResponse<String> resetPassword(String oldPassword, String newPassword,User user) {
        int resultCount=userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword),user.getId());
        if(resultCount==0){
           return ServiceResponse.createByErrorMessage("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        resultCount=userMapper.updateByPrimaryKeySelective(user);
        if(resultCount==0){
            return ServiceResponse.createByErrorMessage("密码更新失败");
        }
        return ServiceResponse.createBySuccessMessage("密码更新成功");
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @Override
    public ServiceResponse<User> updateUserInfo(User user) {
        int resultCount=userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount>0){
            return ServiceResponse.createByErrorMessage("email已存在");
        }

        User updateUser=new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        resultCount=userMapper.updateByPrimaryKeySelective(updateUser);
        if(resultCount==0){
            return ServiceResponse.createByErrorMessage("用户信息更新失败");
        }

        return ServiceResponse.createBySuccess("用户信息更新成功",updateUser);
    }

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    @Override
    public ServiceResponse<User> getInformation(Integer id) {
        User user= userMapper.selectByPrimaryKey(id);
        if(user==null){
            return ServiceResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createBySuccess(user);
    }


    /**
     * 检查用户是否为管理员
     * @param user
     * @return
     */
    @Override
    public ServiceResponse checkAdminRole(User user) {
        if(user!=null && user.getRole()==Const.Role.ROLE_ADMIN){
            return ServiceResponse.createBySuccess();
        }
        return ServiceResponse.createByErrorMessage("用户不是管理员");
    }


}

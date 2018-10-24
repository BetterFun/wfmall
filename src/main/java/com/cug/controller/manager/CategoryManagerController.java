package com.cug.controller.manager;

import com.cug.common.Const;
import com.cug.common.ServiceResponse;
import com.cug.pojo.Category;
import com.cug.pojo.User;
import com.cug.service.ICategoryService;
import com.cug.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Administrator on 2018/9/26 0026.
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryManagerController {

    @Autowired
    private ICategoryService iCategoryService;

    @Autowired
    private IUserService iUserService;

    /**
     * 添加类目
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServiceResponse addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue = "0") Integer parentId){

        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.addCategory(categoryName,parentId);

        }else {
            return ServiceResponse.createByErrorMessage("不是管理员，无权限操作");
        }
    }

    /**
     * 修改类目名
     * @param session
     * @param categoryName
     * @param categoryId
     * @return
     */
    @RequestMapping("set_categoryName.do")
    @ResponseBody
    public ServiceResponse setCategoryName(HttpSession session,String categoryName, @RequestParam(value = "categoryId",defaultValue = "0")Integer categoryId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.setCategoryName(categoryName,categoryId);
        }else {
            return ServiceResponse.createByErrorMessage("不是管理员，无权限操作");
        }
    }

    @RequestMapping("get_children_parallel_category.do")
    @ResponseBody
    public ServiceResponse<List<Category>> getChildrenParallelCategory(HttpSession session, Integer categoryId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }else {
            return ServiceResponse.createByErrorMessage("不是管理员，无权限操作");
        }
    }

    @RequestMapping("get_all_children_category.do")
    @ResponseBody
    public ServiceResponse getAllChildrenCategory(HttpSession session,  @RequestParam(value = "categoryId",defaultValue = "0")Integer categoryId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.getAllChildrenCategory(categoryId);
        }else {
            return ServiceResponse.createByErrorMessage("不是管理员，无权限操作");
        }
    }
}

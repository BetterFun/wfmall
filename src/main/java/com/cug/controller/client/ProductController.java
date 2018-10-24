package com.cug.controller.client;

import com.cug.common.Const;
import com.cug.common.ServiceResponse;
import com.cug.pojo.User;
import com.cug.service.IProductService;
import com.cug.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/10/13 0013.
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;
    @Autowired
    private IUserService iUserService;

    @RequestMapping("product_detail.do")
    @ResponseBody
    public ServiceResponse productDetail(HttpSession session,Integer productId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        return iProductService.getProductDetail(productId,user);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServiceResponse listProduct(HttpSession session, Integer categoryId, String keyword, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,@RequestParam(value = "orderBy",defaultValue="") String orderBy){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        return iProductService.clientListProduct(categoryId,keyword,pageNum,pageSize,orderBy);
    }
}

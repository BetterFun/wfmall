package com.cug.controller.client;

import com.cug.common.Const;
import com.cug.common.ServiceResponse;
import com.cug.pojo.User;
import com.cug.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/10/19 0019.
 */
@Controller
@RequestMapping("/client/cart/")
public class CartController {
    @Autowired
    private ICartService iCartService;

    @RequestMapping("add_cart.do")
    @ResponseBody
    public ServiceResponse addCart(HttpSession session,Integer productId,Integer count){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        return iCartService.addCart(productId,count,user.getId());
    }

    @RequestMapping("list_cart.do")
    @ResponseBody
    public ServiceResponse listCart(HttpSession session){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        return iCartService.listCart(user.getId());
    }

    @RequestMapping("update_cart.do")
    @ResponseBody
    public ServiceResponse updateCart(HttpSession session,Integer productId,Integer count){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        return iCartService.updateCart(user.getId(),productId,count);
    }

    @RequestMapping("delete_cart.do")
    @ResponseBody
    public ServiceResponse deleteCart(HttpSession session,String productIds){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        return iCartService.deleteCart(user.getId(),productIds);
    }

}

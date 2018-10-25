package com.cug.controller.client;

import com.cug.common.Const;
import com.cug.common.ServiceResponse;
import com.cug.pojo.Shipping;
import com.cug.pojo.User;
import com.cug.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/10/25 0025.
 */
@Controller
@RequestMapping("/client/shipping/")
public class ShippingController {
    @Autowired
    private IShippingService iShippingService;

    @RequestMapping("add_shipping.do")
    @ResponseBody
    public ServiceResponse addShipping(HttpSession session, Shipping shipping){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        return iShippingService.addShipping(user.getId(),shipping);
    }

    @RequestMapping("delete_shipping.do")
    @ResponseBody
    public ServiceResponse deleteShipping(HttpSession session, Integer shippingId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        return iShippingService.deleteShipping(user.getId(),shippingId);
    }

    @RequestMapping("update_shipping.do")
    @ResponseBody
    public ServiceResponse updateShipping(HttpSession session, Shipping shipping){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        return iShippingService.updateShipping(user.getId(),shipping);
    }

    @RequestMapping("select_shipping.do")
    @ResponseBody
    public ServiceResponse selectShipping(HttpSession session, Integer shippingId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        return iShippingService.selectShipping(user.getId(),shippingId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServiceResponse list(HttpSession session, @RequestParam(value = "pageNum" , defaultValue = "1") Integer pageNum,@RequestParam(value = "pageSize" , defaultValue = "10") Integer pageSize){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        return iShippingService.list(user.getId(),pageNum,pageSize);
    }
}

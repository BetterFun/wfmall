package com.cug.controller.manager;

import com.cug.common.Const;
import com.cug.common.ServiceResponse;
import com.cug.pojo.Product;
import com.cug.pojo.User;
import com.cug.service.IFileService;
import com.cug.service.IProductService;
import com.cug.service.IUserService;
import com.cug.util.PropertiesUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/28 0028.
 */
@Controller
@RequestMapping("/manage/product/")
public class ProductManagerController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    @RequestMapping("save.do")
    @ResponseBody
    public ServiceResponse saveOrUpdateProduct(HttpSession session, Product product){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }

        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.saveOrUpdateProduct(product);
        }else {
            return ServiceResponse.createByErrorMessage("用户不是管理员，无管理员权限");
        }
    }

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServiceResponse setSaleStatus(HttpSession session,Integer productId, Integer status){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }

        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.setSaleStatus(productId,status);
        }else {
            return ServiceResponse.createByErrorMessage("用户不是管理员，无管理员权限");
        }
    }

    @RequestMapping("get_product_detail.do")
    @ResponseBody
    public ServiceResponse getProductDetail(HttpSession session,Integer productId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        return iProductService.getProductDetail(productId,user);
    }

    @RequestMapping("get_product_list.do")
    @ResponseBody
    public ServiceResponse getProductList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }

        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.getProductList(pageNum,pageSize);
        }else {
            return ServiceResponse.createByErrorMessage("用户不是管理员，无管理员权限");
        }
    }

    @RequestMapping("search_product_list.do")
    @ResponseBody
    public ServiceResponse searchProductList(HttpSession session,Integer productId,String productName, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }

        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.searchProductList(productId,productName,pageNum,pageSize);
        }else {
            return ServiceResponse.createByErrorMessage("用户不是管理员，无管理员权限");
        }
    }

    @RequestMapping("upload.do")
    @ResponseBody
    public ServiceResponse upload(HttpSession session,MultipartFile file, HttpServletRequest request){
        /*User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }*/

        if(true){
            String path=request.getSession().getServletContext().getRealPath("upload");
            String targetFileName=iFileService.upload(file,path);
            String url= PropertiesUtil.getValue("ftp.server.http.prefix")+targetFileName;
            Map result= Maps.newHashMap();
            result.put("uri",targetFileName);
            result.put("url",url);
            return ServiceResponse.createBySuccess(result);
        }else {
            return ServiceResponse.createByErrorMessage("用户不是管理员，无管理员权限");
        }

    }

    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, MultipartFile file, HttpServletResponse response, HttpServletRequest request){
        Map result=Maps.newHashMap();
        User user= (User) session.getAttribute(Const.CURRENT_USER);
       /* if(user==null){
            result.put("success",false);
            result.put("msg","用户未登录");
            return result;
        }*/
        if(true){
            String path=request.getSession().getServletContext().getRealPath("upload");
            String targetFileName=iFileService.upload(file,path);
            if(StringUtils.isBlank(targetFileName)){
                result.put("success",false);
                result.put("msg","上传失败");
                result.put("file_path", "[real file path]");
                return result;
            }
            String url= PropertiesUtil.getValue("ftp.server.http.prefix")+targetFileName;
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            result.put("success",true);
            result.put("msg","上传成功");
            result.put("file_path",url);
            return result;
        }else {
            result.put("success",false);
            result.put("msg","用户不是管理员，无管理员权限");
            return result;
        }
    }
}

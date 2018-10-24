package com.cug.service.impl;

import com.cug.common.Const;
import com.cug.common.ServiceResponse;
import com.cug.dao.CategoryMapper;
import com.cug.dao.ProductMapper;
import com.cug.pojo.Category;
import com.cug.pojo.Product;
import com.cug.pojo.User;
import com.cug.service.ICategoryService;
import com.cug.service.IProductService;
import com.cug.service.IUserService;
import com.cug.util.DateTimeUtil;
import com.cug.util.PropertiesUtil;
import com.cug.vo.ProductDetailVo;
import com.cug.vo.ProductListVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/9/28 0028.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService{

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ICategoryService iCategoryService;
    @Autowired
    private IUserService iUserService;

    @Override
    public ServiceResponse saveOrUpdateProduct(Product product) {
        if(product!=null){
            if(StringUtils.isNotBlank(product.getSubImages())){
                String[] Images=product.getSubImages().split(",");
                if(Images.length>0){
                    product.setMainImage(Images[0]);
                }
            }
            if(product.getId()!=null){
                int resultCount= productMapper.updateByPrimaryKey(product);
                if(resultCount>0){
                    return ServiceResponse.createBySuccessMessage("更新商品成功");
                }else {
                    return ServiceResponse.createBySuccessMessage("更新商品失败");
                }
            }else {
                int resultCount= productMapper.insert(product);
                if(resultCount>0){
                    return ServiceResponse.createBySuccessMessage("添加商品成功");
                }else {
                    return ServiceResponse.createBySuccessMessage("添加商品失败");
                }
            }
        }
        return ServiceResponse.createByErrorMessage("商品参数错误");
    }

    @Override
    public ServiceResponse setSaleStatus(Integer productId, Integer status) {
        if(productId==null || status==null){
            return ServiceResponse.createBySuccessMessage("商品参数错误");
        }
        Product product=new Product();
        product.setId(productId);
        product.setStatus(status);
        int resultCount=productMapper.updateByPrimaryKeySelective(product);
        if(resultCount>0){
            return ServiceResponse.createBySuccessMessage("修改商品状态成功");
        }
        return ServiceResponse.createByErrorMessage("修改商品状态失败");
    }

    @Override
    public ServiceResponse getProductDetail(Integer productId,User user) {
        if(productId==null){
            return ServiceResponse.createBySuccessMessage("商品参数错误");
        }
        Product product=new Product();
        if(!iUserService.checkAdminRole(user).isSuccess()){
            product=productMapper.selectByPrimaryKeyAndStatus(productId);
        }
        product=productMapper.selectByPrimaryKey(productId);
        if(product!=null){
            ProductDetailVo productDetailVo=assembleProductDetail(product);
            return ServiceResponse.createBySuccess(productDetailVo);
        }
        return ServiceResponse.createByErrorMessage("此商品不存在");
    }

    private ProductDetailVo assembleProductDetail(Product product){
        if(product==null){
            return null;
        }else {
            ProductDetailVo productDetailVo=new ProductDetailVo();
            productDetailVo.setId(product.getId());
            productDetailVo.setCategoryId(product.getCategoryId());
            productDetailVo.setName(product.getName());
            productDetailVo.setSubtitle(product.getSubtitle());
            productDetailVo.setMainImage(product.getMainImage());
            productDetailVo.setSubImages(product.getSubImages());
            productDetailVo.setPrice(product.getPrice());
            productDetailVo.setStatus(product.getStatus());
            productDetailVo.setStock(product.getStock());

            productDetailVo.setImageHost(PropertiesUtil.getValue("ftp.server.http.prefix"));

            Category category=categoryMapper.selectByPrimaryKey(product.getCategoryId());
            if(category==null){
                productDetailVo.setCategoryParentId(0);
            }else {
                productDetailVo.setCategoryParentId(category.getParentId());
            }

            productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime(),"yyyy-MM-dd HH-mm-ss"));
            productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime(),"yyyy-MM-dd HH-mm-ss"));

            return productDetailVo;
        }
    }

    @Override
    public ServiceResponse getProductList(Integer pageNum, Integer pageSize) {
        if(pageNum == null || pageSize==null){
            return ServiceResponse.createBySuccessMessage("分页参数错误");
        }
        PageHelper.startPage(pageNum,pageSize);

        List<Product> productList=productMapper.selectProductList();
        List<ProductListVo> productVoList= Lists.newArrayList();
        for (Product productItem:productList){
            ProductListVo productListVo=assembleProductList(productItem);
            productVoList.add(productListVo);
        }

        PageInfo pageInfo=new PageInfo(productVoList);
        return ServiceResponse.createBySuccess(pageInfo);
    }

    private ProductListVo assembleProductList(Product product){
        if(product==null){
            return null;
        }else {
            ProductListVo productListVo=new ProductListVo();
            productListVo.setId(product.getId());
            productListVo.setCategoryId(product.getCategoryId());
            productListVo.setName(product.getName());
            productListVo.setSubtitle(product.getSubtitle());
            productListVo.setMainImage(product.getMainImage());
            productListVo.setStatus(product.getStatus());
            productListVo.setPrice(product.getPrice());

            return productListVo;
        }
    }

    @Override
    public ServiceResponse searchProductList(Integer productId, String productName, Integer pageNum, Integer pageSize) {
        if(pageNum == null || pageSize==null){
            return ServiceResponse.createBySuccessMessage("分页参数错误");
        }
        if(StringUtils.isNotBlank(productName)){
            productName=new StringBuffer().append("%").append(productName).append("%").toString();
        }
        PageHelper.startPage(pageNum,pageSize);

        List<Product> productList=productMapper.selectByNameAndProductId(productId,productName);
        List<ProductListVo> productVoList= Lists.newArrayList();
        for (Product productItem:productList){
            ProductListVo productListVo=assembleProductList(productItem);
            productVoList.add(productListVo);
        }

        PageInfo pageInfo=new PageInfo(productVoList);
        return ServiceResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServiceResponse clientListProduct(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        if(pageNum == null || pageSize==null){
            return ServiceResponse.createBySuccessMessage("分页参数错误");
        }

        //以keyword为主，只要keyword不为空就搜索条件
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(keyword)){
            keyword=new StringBuffer().append("%").append(keyword).append("%").toString();
        }

        List<Integer> categoryIdList=Lists.newArrayList();
        if(categoryId != null ){
            Category category=categoryMapper.selectByPrimaryKey(categoryId);
            if(category == null &&StringUtils.isBlank(keyword)){
                //没有条件上的类目，并且关键字为空时返回空的商品
                List<ProductListVo> productListVoList=Lists.newArrayList();
                PageInfo pageInfo=new PageInfo(productListVoList);
                return ServiceResponse.createBySuccess(pageInfo);
            }
            categoryIdList= (List<Integer>) iCategoryService.getAllChildrenCategory(category.getId()).getData();
        }
        //排序
        if(StringUtils.isNotBlank(orderBy)){
            if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] orderByArray=orderBy.split("_");
                PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
            }
        }
        List<Product> productList=productMapper.selectByNameAndCategoryIdList(categoryIdList,keyword);
        List<ProductListVo> productVoList= Lists.newArrayList();
        for (Product productItem:productList){
            ProductListVo productListVo=assembleProductList(productItem);
            productVoList.add(productListVo);
        }
        PageInfo pageInfo=new PageInfo(productVoList);
        return ServiceResponse.createBySuccess(pageInfo);
    }
}

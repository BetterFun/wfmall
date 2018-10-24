package com.cug.service;

import com.cug.common.ServiceResponse;
import com.cug.pojo.Product;
import com.cug.pojo.User;

/**
 * Created by Administrator on 2018/9/28 0028.
 */
public interface IProductService {
    ServiceResponse saveOrUpdateProduct(Product product);

    ServiceResponse setSaleStatus(Integer id, Integer status);

    ServiceResponse getProductDetail(Integer productId, User user);

    ServiceResponse getProductList(Integer pageNum, Integer pageSize);

    ServiceResponse searchProductList(Integer productId, String productName, Integer pageNum, Integer pageSize);

    ServiceResponse clientListProduct(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);
}

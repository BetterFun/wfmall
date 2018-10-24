package com.cug.dao;

import com.cug.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectProductList();

    List<Product> selectByNameAndProductId(@Param("productId") Integer productId, @Param("productName") String productName);

    List<Product> selectByNameAndCategoryIdList(@Param("categoryIdList") List<Integer> categoryIdList, @Param("productName") String productName);

    Product selectByPrimaryKeyAndStatus(Integer productId);
}
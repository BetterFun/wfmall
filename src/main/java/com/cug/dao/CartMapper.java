package com.cug.dao;

import com.cug.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    List<Cart> selectByUserId(Integer userId);

    Cart selectByUserIdAndProductId(@Param("userId") Integer userId,@Param("productId") Integer productId);

    int selectAllCheckByUserId(Integer userId);

    void deleteByUserIdAndProductIds(@Param("userId")Integer userId,@Param("productIdList") List<Integer> productIdList);

    int checkedOrUnCheckedProduct(@Param("userId")Integer userId,@Param("productId") Integer productId,@Param("checked") Integer checked);

    int selectCartProductCount(Integer userId);
}
package com.cug.dao;

import com.cug.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteShippingByUserId(@Param("userId") Integer userId,@Param("shippingId") Integer shippingId);

    int updateShippingByUserId(Shipping record);

    Shipping selectShippingByUserId(@Param("userId")Integer userId,@Param("shippingId") Integer shippingId);

    List<Shipping> selectAllShippingByUserId(Integer userId);
}
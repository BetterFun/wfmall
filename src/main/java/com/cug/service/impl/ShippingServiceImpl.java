package com.cug.service.impl;

import com.cug.common.ServiceResponse;
import com.cug.dao.ShippingMapper;
import com.cug.pojo.Shipping;
import com.cug.service.IShippingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/10/25 0025.
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService{

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServiceResponse addShipping(Integer userId,Shipping shipping) {
        if(userId == null ||shipping == null){
            return ServiceResponse.createByErrorMessage("参数错误");
        }
        shipping.setUserId(userId);
        int resultCount=shippingMapper.insert(shipping);
        if(resultCount>0){
            Map map= Maps.newHashMap();
            map.put("shipping",shipping.getId());
            return ServiceResponse.createBySuccess("新建地址成功",map);
        }else {
            return ServiceResponse.createByErrorMessage("新建地址失败");
        }
    }

    @Override
    public ServiceResponse deleteShipping(Integer userId, Integer shippingId) {
        if(userId == null || shippingId == null){
            return ServiceResponse.createByErrorMessage("参数错误");
        }
        int resultCount=shippingMapper.deleteShippingByUserId(userId,shippingId);
        if(resultCount>0){
            return ServiceResponse.createBySuccess("删除地址成功");
        }else {
            return ServiceResponse.createByErrorMessage("删除地址失败");
        }
    }

    @Override
    public ServiceResponse updateShipping(Integer userId, Shipping shipping) {
        if(userId == null || shipping == null){
            return ServiceResponse.createByErrorMessage("参数错误");
        }
        shipping.setUserId(userId);
        int resultCount=shippingMapper.updateShippingByUserId(shipping);
        if(resultCount>0){
            return ServiceResponse.createBySuccess("更新地址成功");
        }else {
            return ServiceResponse.createByErrorMessage("更新地址失败");
        }
    }

    @Override
    public ServiceResponse selectShipping(Integer userId, Integer shippingId) {
        if(userId == null || shippingId == null){
            return ServiceResponse.createByErrorMessage("参数错误");
        }
        Shipping shipping=shippingMapper.selectShippingByUserId(userId,shippingId);
        if(shipping != null){
            return ServiceResponse.createBySuccess(shipping);
        }else {
            return ServiceResponse.createByErrorMessage("查询地址失败");
        }
    }

    @Override
    public ServiceResponse list(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList= shippingMapper.selectAllShippingByUserId(userId);
        PageInfo pageInfo=new PageInfo(shippingList);
        return ServiceResponse.createBySuccess(pageInfo);
    }
}

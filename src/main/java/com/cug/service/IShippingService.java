package com.cug.service;

import com.cug.common.ServiceResponse;
import com.cug.pojo.Shipping;

/**
 * Created by Administrator on 2018/10/25 0025.
 */
public interface IShippingService {
    ServiceResponse addShipping(Integer userId,Shipping shipping);

    ServiceResponse deleteShipping(Integer userId, Integer shippingId);

    ServiceResponse updateShipping(Integer userId, Shipping shipping);

    ServiceResponse selectShipping(Integer userId, Integer shippingId);

    ServiceResponse list(Integer userId, Integer pageNum, Integer pageSize);
}

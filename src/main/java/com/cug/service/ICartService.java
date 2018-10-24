package com.cug.service;

import com.cug.common.ServiceResponse;

/**
 * Created by Administrator on 2018/10/19 0019.
 */
public interface ICartService {
    ServiceResponse addCart(Integer productId, Integer count, Integer userId);

    ServiceResponse listCart(Integer userId);

    ServiceResponse updateCart(Integer userId, Integer productId, Integer count);

    ServiceResponse deleteCart(Integer userId, String productIds);
}

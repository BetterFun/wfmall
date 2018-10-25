package com.cug.service.impl;

import com.cug.common.Const;
import com.cug.common.ServiceResponse;
import com.cug.dao.CartMapper;
import com.cug.dao.ProductMapper;
import com.cug.pojo.Cart;
import com.cug.pojo.Product;
import com.cug.service.ICartService;
import com.cug.util.BigDecimalUtil;
import com.cug.util.PropertiesUtil;
import com.cug.vo.CartProductVo;
import com.cug.vo.CartVo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/10/19 0019.
 */
@Service("iCartService")
public class CartService implements ICartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServiceResponse addCart(Integer productId, Integer count, Integer userId) {
        if(productId==null || count==null){
            return ServiceResponse.createByErrorMessage("参数错误");
        }

        //查找此用户有没有该商品的购物车
        Cart cart=cartMapper.selectByUserIdAndProductId(userId,productId);
        if(cart==null){
            //购物车里没有此商品,需要添加此商品
            cart=new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setChecked(Const.Cart.CHECKED);
            cart.setQuantity(count);//前台应该已经检查过传入商品的数量不能超过库存
            cartMapper.insert(cart);
        }else {
            //购物车里有此商品，更新购物车中商品的数量
            cart.setQuantity(count+cart.getQuantity());
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.listCart(userId);
    }

    @Override
    public ServiceResponse listCart(Integer userId) {
        CartVo cartVo=getCartVo(userId);
        return ServiceResponse.createBySuccess(cartVo);
    }

    @Override
    public ServiceResponse updateCart(Integer userId, Integer productId, Integer count) {
        if(productId==null || count==null){
            return ServiceResponse.createByErrorMessage("参数错误");
        }
        Cart cart=cartMapper.selectByUserIdAndProductId(userId,productId);
        if(cart != null){
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return this.listCart(userId);
    }

    @Override
    public ServiceResponse deleteCart(Integer userId, String productIds) {
        if(userId == null || productIds.isEmpty()){
            return ServiceResponse.createByErrorMessage("参数错误");
        }
        String[] productId=productIds.split(",");
        List<Integer> productIdList=Lists.newArrayList();
        for(int i=0;i<productId.length;++i){
            productIdList.add(Integer.valueOf(productId[i]));
        }
        cartMapper.deleteByUserIdAndProductIds(userId,productIdList);
        return this.listCart(userId);
    }

    @Override
    public ServiceResponse selectOrUnSelect(Integer userId,Integer productId,Integer checked){
        cartMapper.checkedOrUnCheckedProduct(userId,productId,checked);
        return this.listCart(userId);
    }

    @Override
    public ServiceResponse getCartProductCount(Integer userId) {
        if(userId == null){
            return ServiceResponse.createBySuccess(0);
        }
        int count=cartMapper.selectCartProductCount(userId);
        return ServiceResponse.createBySuccess(count);
    }

    private CartVo getCartVo(Integer userId){
        List<Cart> cartList=cartMapper.selectByUserId(userId);
        if(cartList == null){
            return null;
        }

        BigDecimal cartTotalPrice=new BigDecimal("0");
        List<CartProductVo> cartProductVoList=Lists.newArrayList();
        for (Cart cartItem:cartList){
            CartProductVo cartProductVo=new CartProductVo();
            Product product=productMapper.selectByPrimaryKeyAndStatus(cartItem.getProductId());
            if(product == null){
                cartProductVo=null;
            }else {
                //将Cart转化为CartProductVo
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(cartItem.getUserId());
                cartProductVo.setProductId(cartItem.getProductId());
                cartProductVo.setProductName(product.getName());
                cartProductVo.setProductSubtitle(product.getSubtitle());
                cartProductVo.setProductMainImage(product.getMainImage());
                cartProductVo.setProductChecked(cartItem.getChecked());
                cartProductVo.setProductPrice(product.getPrice());
                cartProductVo.setProductStatus(product.getStatus());
                cartProductVo.setProductStock(product.getStock());
                //获取每条购物车中商品的总量
                if(cartItem.getQuantity()>product.getStock()){
                    cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                    cartItem.setQuantity(product.getStock());
                    cartMapper.updateByPrimaryKeySelective(cartItem);
                }else {
                    cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                }
                cartProductVo.setQuantity(cartItem.getQuantity());
                cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(cartItem.getQuantity(),product.getPrice().doubleValue()));

                //计算购物车总价
                if(cartProductVo.getProductChecked() == 1){
                    cartTotalPrice=cartTotalPrice.add(cartProductVo.getProductTotalPrice());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        CartVo cartVo=new CartVo();
        cartVo.setCartVoList(cartProductVoList);
        cartVo.setAllChecked(getAllChecked(userId));
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setHostImage(PropertiesUtil.getValue("ftp.server.http.prefix"));

        return cartVo;
    }

    private boolean getAllChecked(Integer userId){
        if(userId == null){
            return false;
        }
        if(cartMapper.selectAllCheckByUserId(userId)>0){
            return false;
        }
        return true;
    }
}

package com.cug.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2018/10/19 0019.
 */
public class CartVo {
    private List<CartProductVo> cartProductVoList;
    private boolean allChecked;
    private BigDecimal cartTotalPrice;

    private String hostImage;

    public List<CartProductVo> getCartVoList() {
        return cartProductVoList;
    }

    public void setCartVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public boolean isAllChecked() {
        return allChecked;
    }

    public void setAllChecked(boolean allChecked) {
        this.allChecked = allChecked;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public String getHostImage() {
        return hostImage;
    }

    public void setHostImage(String hostImage) {
        this.hostImage = hostImage;
    }
}

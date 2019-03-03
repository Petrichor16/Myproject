package com.ombre.woodhouse.Bean;

/**
 * Created by OMBRE on 2017/12/4.
 */

//我的收藏表
public class Footprint {
    private String goodsID;//商品编号
    private String userPhone;//账户号

    public Footprint() {
    }

    public Footprint(String goodsID, String userPhone) {
        this.goodsID = goodsID;
        this.userPhone = userPhone;
    }

    public String getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(String goodsID) {
        this.goodsID = goodsID;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}

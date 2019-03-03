package com.ombre.woodhouse.Bean.Item;

import android.graphics.Bitmap;

/**
 * Created by OMBRE on 2017/12/18.
 */

//购物车栏 或者订单商品列表栏  布局item实体类
public class CartItem {
    int id;
    private int  goodsID;//商品编号
    private String cartpicturePath;//展示图片
    private String cart_comm_name;//名称
    private int values;//选择的数量
    private double cartprice;//价格
    private boolean isselect;//选项框
    public CartItem(int id,int goodsID, String cartpicturePath, String cart_comm_name, int values, double cartprice, boolean isselect) {
        this.id=id;
        this.goodsID = goodsID;
        this.cartpicturePath = cartpicturePath;
        this.cart_comm_name = cart_comm_name;
        this.values = values;
        this.cartprice = cartprice;
        this.isselect = isselect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(int goodsID) {
        this.goodsID = goodsID;
    }

    public String getCartpicturePath() {
        return cartpicturePath;
    }

    public void setCartpicturePath(String cartpicturePath) {
        this.cartpicturePath = cartpicturePath;
    }

    public String getCart_comm_name() {
        return cart_comm_name;
    }

    public void setCart_comm_name(String cart_comm_name) {
        this.cart_comm_name = cart_comm_name;
    }

    public int getValues() {
        return values;
    }

    public void setValues(int values) {
        this.values = values;
    }

    public double getCartprice() {
        return cartprice;
    }

    public void setCartprice(double cartprice) {
        this.cartprice = cartprice;
    }

    public boolean isselect() {
        return isselect;
    }

    public void setIsselect(boolean isselect) {
        this.isselect = isselect;
    }
}

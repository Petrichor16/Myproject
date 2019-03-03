package com.ombre.woodhouse.Bean;

/**
 * Created by OMBRE on 2018/6/18.
 */

//收藏实体
public class Collections {
    private Integer id;
    private String userPhone;
    private String goodsID;

    public Collections(Integer id, String userPhone, String goodsID) {
        this.id = id;
        this.userPhone = userPhone;
        this.goodsID = goodsID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(String goodsID) {
        this.goodsID = goodsID;
    }
}

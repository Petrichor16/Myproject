package com.ombre.woodhouse.Bean.Item;

/**
 * Created by OMBRE on 2018/6/6.
 */

//首页展示部分商品
public class PartGoodsItem {
    int goodsId;//商品编号
    String goodsName;
    String goodsPath;

    public PartGoodsItem() {
    }

    public PartGoodsItem(int goodsId, String goodsPath, String goodsName) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsPath = goodsPath;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPath() {
        return goodsPath;
    }

    public void setGoodsPath(String goodsPath) {
        this.goodsPath = goodsPath;

    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }
}

package com.ombre.woodhouse.Bean.Item;

import android.graphics.Bitmap;

/**
 * Created by OMBRE on 2017/12/10.
 */
//商品卡片显示
public class CommCarditem {
    int commId;
    private String comm_card_exhibition;//卡片的展示图片地址
    private String comm_state;//商品的新旧状态
    private String comm_card_name;//商品的名称
    private double comm_card_price;//商品的价格
    private Integer comm_values;//销量

    public CommCarditem() {
    }

    public CommCarditem(int commId, String comm_card_exhibition, String comm_state, String comm_card_name, double comm_card_price, Integer comm_values) {
        this.commId=commId;
        this.comm_card_exhibition = comm_card_exhibition;
        this.comm_state = comm_state;
        this.comm_card_name = comm_card_name;
        this.comm_card_price = comm_card_price;
        this.comm_values = comm_values;
    }

    public String getComm_card_exhibition() {
        return comm_card_exhibition;
    }

    public void setComm_card_exhibition(String comm_card_exhibition) {
        this.comm_card_exhibition = comm_card_exhibition;
    }

    public int getCommId() {
        return commId;
    }

    public void setCommId(int commId) {
        this.commId = commId;
    }

    public String getComm_state() {
        return comm_state;
    }

    public void setComm_state(String comm_state) {
        this.comm_state = comm_state;
    }

    public String getComm_card_name() {
        return comm_card_name;
    }

    public void setComm_card_name(String comm_card_name) {
        this.comm_card_name = comm_card_name;
    }

    public double getComm_card_price() {
        return comm_card_price;
    }

    public void setComm_card_price(double comm_card_price) {
        this.comm_card_price = comm_card_price;
    }

    public Integer getComm_values() {
        return comm_values;
    }

    public void setComm_values(Integer comm_values) {
        this.comm_values = comm_values;
    }
}

package com.ombre.woodhouse.Bean;


import java.io.Serializable;


public class Picture implements Serializable {
    private int id;
    private int goodsID;
    private String picturePath;
    private Integer pictureType;

    public Picture(int id, int goodsID, String picturePath, Integer pictureType) {
        this.id = id;
        this.goodsID = goodsID;
        this.picturePath = picturePath;
        this.pictureType = pictureType;
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

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public Integer getPictureType() {
        return pictureType;
    }

    public void setPictureType(Integer pictureType) {
        this.pictureType = pictureType;
    }
}

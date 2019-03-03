package com.ombre.woodhouse.Bean;


import java.io.Serializable;

//评价
public class Evaluation implements Serializable {
    private int id;
    private int goodsId;//商品
    private String userName;//用户名
    private String content;//评论内容
    private String picture;//图片
    private float startLevel;//评价星级

    public Evaluation(int id, int goodsId, String userName, String content, String picture, float startLevel) {
        this.id = id;
        this.goodsId = goodsId;
        this.userName = userName;
        this.content = content;
        this.picture = picture;
        this.startLevel = startLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        if(userName!=null&&userName.length()>=2){
            String name=userName.substring(0,1)+"**"+userName.substring(userName.length()-1,userName.length());
            return  name;
        }else
            return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public float getStartLevel() {
        return startLevel;
    }

    public void setStartLevel(float startLevel) {
        this.startLevel = startLevel;
    }
}

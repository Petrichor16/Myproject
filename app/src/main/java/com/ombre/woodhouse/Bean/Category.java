package com.ombre.woodhouse.Bean;

import java.io.Serializable;

/**
 * Created by OMBRE on 2018/6/2.
 */
//分类表
public class Category implements Serializable{
    private int id;
    private String categoryName;
    private String categoryPic;//二级分类图片地址
    public Category(int id,String categoryName,String categoryPic ) {
        this.id=id;
        this.categoryName = categoryName;
        this.categoryPic=categoryPic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryPic() {
        return categoryPic;
    }

    public void setCategoryPic(String categoryPic) {
        this.categoryPic = categoryPic;
    }
}

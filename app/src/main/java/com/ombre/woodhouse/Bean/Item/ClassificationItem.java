package com.ombre.woodhouse.Bean.Item;

import android.graphics.Bitmap;

/**
 * Created by OMBRE on 2018/5/26.
 */

//二级分类列表item的实体类
public class ClassificationItem{
    int id;
    String  classificationName;//类名
    Bitmap classificationPic;//图片地址
    String picPath;//图片地址

    public ClassificationItem( String classificationName, Bitmap classificationPic) {
        this.classificationName = classificationName;
        this.classificationPic = classificationPic;
    }

    public ClassificationItem(String classificationName, String picPath) {
        this.classificationName = classificationName;
        this.picPath = picPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    public Bitmap getClassificationPic() {
        return classificationPic;
    }

    public void setClassificationPic(Bitmap classificationPPic) {
        this.classificationPic = classificationPPic;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}

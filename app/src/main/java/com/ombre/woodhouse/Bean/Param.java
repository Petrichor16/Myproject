package com.ombre.woodhouse.Bean;


import java.io.Serializable;
//参数实体
public class Param implements Serializable {
    private int id;
    private String paramName;//参数名
    private String paramText;//参数内容

    public Param(int id, String paramName, String paramText) {
        this.id = id;
        this.paramName = paramName;
        this.paramText = paramText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamText() {
        return paramText;
    }

    public void setParamText(String paramText) {
        this.paramText = paramText;
    }
}

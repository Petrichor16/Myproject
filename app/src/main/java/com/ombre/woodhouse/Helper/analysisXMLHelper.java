package com.ombre.woodhouse.Helper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ombre.woodhouse.Bean.Category;
import com.ombre.woodhouse.Bean.Commodity;
import com.ombre.woodhouse.Bean.Evaluation;
import com.ombre.woodhouse.Bean.Item.CartItem;
import com.ombre.woodhouse.Bean.Member;
import com.ombre.woodhouse.Bean.Orders;
import com.ombre.woodhouse.Bean.Param;
import com.ombre.woodhouse.Bean.Picture;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by OMBRE on 2018/5/30.
 */

//xml解析
public  class analysisXMLHelper {
    public analysisXMLHelper() {
    }
    Gson gson=new Gson();
    //用户
    public Member analysisMember (String jsonData){
        Member member=null;
        if(jsonData!=null&&!jsonData.equals("[]")) {
            List<Member> memberList = gson.fromJson(jsonData, new TypeToken<List<Member>>() {
            }.getType());
            if (memberList.size() > 0) {
                member = memberList.get(0);
            }
        }
        return member;
    }
    //所有商品
    public List<Commodity> analysisAllCommodity(String jsonData){
        if(jsonData!=null&&!jsonData.equals("[]")) {
        List<Commodity> commodityList=gson.fromJson(jsonData,new TypeToken<List<Commodity>>(){}.getType());
        if(commodityList.size()>0)
            return commodityList;
        else
            return null;}
        else
          return null;
    }
    //某一件商品
    public Commodity analysisCommodityDetails(String jsonData){

            Commodity commodity = new Commodity();
        if(jsonData!=null&&!jsonData.equals("[]")) {
            List<Commodity> commodityList = gson.fromJson(jsonData, new TypeToken<List<Commodity>>() {
            }.getType());
            if (commodityList.size() > 0)
                commodity = commodityList.get(0);
        }
        return commodity;
    }
    //商品分类
    public List<Category> analysisCategory(String jsonData){
        if(jsonData!=null&&!jsonData.equals("[]")) {
            List<Category> categoryList = gson.fromJson(jsonData, new TypeToken<List<Category>>() {
            }.getType());
            if (categoryList.size() > 0)
                return categoryList;
            else
                return null;
        }
        else
            return null;
    }
    //商品图片
    public List<Picture> analysisGoodsPic(String jsonData){
        if(jsonData!=null&&!jsonData.equals("[]")) {
            List<Picture> pictureList = gson.fromJson(jsonData, new TypeToken<List<Picture>>() {
            }.getType());
            if (pictureList.size() > 0)
                return pictureList;
            else
                return null;
        }
        else
            return null;
    }
    //商品参数
    public List<Param> analysisGoodsParam(String jsonData){
        if(jsonData!=null&&!jsonData.equals("[]")) {
            List<Param> paramList = gson.fromJson(jsonData, new TypeToken<List<Param>>() {
            }.getType());
            if (paramList.size() > 0)
                return paramList;
            else
                return null;
        }
        else
            return null;
    }
    //商品评价
    public List<Evaluation> analysisGoodsEvaluation(String jsonData){
        if(jsonData!=null&&!jsonData.equals("[]")) {
            List<Evaluation> evaluations = gson.fromJson(jsonData, new TypeToken<List<Evaluation>>() {
            }.getType());
            if (evaluations.size() > 0)
                return evaluations;
            else
                return null;
        }
        else
            return null;
    }
    //购物车
    public List<CartItem> analysisGoodsCart(String jsonData){
        if(jsonData!=null&&!jsonData.equals("[]")) {
            List<CartItem> cartItemList = gson.fromJson(jsonData, new TypeToken<List<CartItem>>() {}.getType());
            if (cartItemList.size() > 0)
                return cartItemList;
            else
                return null;
        }
        else
            return null;
    }
    //解析订单详情
    public List<Orders> analysisOrderDetails(String jsonData){
        List<Orders> ordersList=new ArrayList<>();
        if(jsonData!=null&&!jsonData.equals("[]")) {
             ordersList = gson.fromJson(jsonData, new TypeToken<List<Orders>>(){}.getType());
        }
        return ordersList;
    }
}

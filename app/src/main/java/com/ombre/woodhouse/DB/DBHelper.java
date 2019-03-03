package com.ombre.woodhouse.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by OMBRE on 2017/11/16.
 */
//创建数据库
public class DBHelper extends SQLiteOpenHelper {

     //地址表属性有主键id，账户号，姓名，收货电话，地址以及备注  14
    public static final String CREATE_TB_ADDRESS="create table tb_address" +
            "(address_id Integer primary key autoIncrement,userPhone text,username text,user_phone text,area text,address_remark text)" ;
    // 搜索记录实体表属性有主键id 用户名和每次搜索是输入的内容  19
    public static final String CREATE_TB_SEARCHRECORD="create table tb_searchrecord" +
            "(search_id Integer primary key autoIncrement,userPhone text ,search_content text)";
    //足迹表：用户号 和 商品编号
    public static final String CREATE_TB_FOOTPRINT="create table tb_footprint" +
            "(choice_id Integer primary key autoIncrement,userPhone text,goodsID Integer)";
    private Context myContext;//上下文
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
         super(context, name, factory, version);
        myContext=context;//链接上下文
    }
    @Override
    public void onCreate(SQLiteDatabase db_WHShopping) {
      //创建数据表
        db_WHShopping.execSQL(CREATE_TB_ADDRESS);
        db_WHShopping.execSQL(CREATE_TB_SEARCHRECORD);
        db_WHShopping.execSQL(CREATE_TB_FOOTPRINT);
    }

    //升级数据库  newVersion
    @Override
    public void onUpgrade(SQLiteDatabase db_WHShopping, int oldVersion, int newVersion) {
        switch(newVersion){
            case 1:
                onCreate(db_WHShopping);
                break;
        }
    }
}

package com.ombre.woodhouse.DB.DBManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.ombre.woodhouse.Bean.Footprint;
import com.ombre.woodhouse.DB.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMBRE on 2017/12/4.
 */

//足迹表管理
public class Footprint_Manager {
    private final static int VERSION=1;//数据库版本
    private final static  String DB_NAME="WHshopping.db";//数据库名
    private DBHelper dbHelper;//创建一个数据库对象
    private SQLiteDatabase db;//创建一个SQLiteDatabase对象，用户实现增删查改的操作
    public Footprint_Manager(Context context) {
        try{dbHelper = new DBHelper(context,DB_NAME,null,VERSION);
            db = dbHelper.getWritableDatabase();}catch (Exception e){
            e.printStackTrace();
        }
    }
    //添加信息至表中
    public  void insertFootprint(ContentValues values,String userPhone,int goodsID){

        try{
            String sql = "select * from tb_footprint where userPhone=? and goodsID=?";
            Cursor cursor=db.rawQuery(sql,new String[]{userPhone, String.valueOf(goodsID)});
            if(!cursor.moveToNext()) {
                db.insert("tb_footprint", null, values);
            }
            db.close();
        }catch (Exception e){e.printStackTrace();}
    }
    //从表中删除数据
    public void deleteFootprint( String WhereClause,String[] WhereArgs){
        try{
            db.delete("tb_footprint",WhereClause,WhereArgs);
        }catch (Exception e){e.printStackTrace();}
    }
    //修改表中的数据
    public  void updateFootprint(ContentValues values,String WhereClause,String WhereArgs){//WhereClause：修改项查找的条件  WhereArgs：条件的名称

        try{
            db.update("tb_footprint",values,WhereClause,new String[]{WhereArgs});
        }catch (Exception e){e.printStackTrace();}
    }

    //查询表中的数据
  public List<Footprint> selectFootprint(String account_numStr){
      Footprint footprint;
      List<Footprint> footprintList =new ArrayList<>();
      try {
          String sql = "select * from tb_footprint where userPhone=?";
          Cursor cursor=db.rawQuery(sql,new String[]{account_numStr});
          if(cursor!=null){
              while (cursor.moveToNext()){
                  String comm_num = cursor.getString(cursor.getColumnIndex("goodsID"));//商品编号
                  footprint=new Footprint(comm_num,account_numStr);
                  footprintList.add(footprint);
              }
          }
      }catch (Exception e){e.printStackTrace();}
      return footprintList;
  }

}

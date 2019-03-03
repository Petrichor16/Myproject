package com.ombre.woodhouse.DB.DBManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.ombre.woodhouse.Bean.Address;
import com.ombre.woodhouse.DB.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMBRE on 2017/12/4.
 */

//地址表管理
public class Address_Manager {
    private final static int VERSION=1;//数据库版本
    private final static  String DB_NAME="WHshopping.db";//数据库名
    private DBHelper dbHelper;//创建一个数据库对象
    private SQLiteDatabase db;//创建一个SQLiteDatabase对象，用户实现增删查改的操作
    public Address_Manager(Context context) {
        try{
            dbHelper = new DBHelper(context,DB_NAME,null,VERSION);
            db = dbHelper.getWritableDatabase();}catch (Exception e){
            e.printStackTrace();
        }
    }
    //添加信息至表中
    public  void insertAddress(ContentValues values){
        try{
            db.insert("tb_address",null,values);
            db.close();
        }catch (Exception e){e.printStackTrace();}
    }
    //从表中删除数据
    public void deleteAddress( String WhereClause,String[] WhereArgs){
        try{
            db.delete("tb_address",WhereClause,WhereArgs);
        }catch (Exception e){e.printStackTrace();}

    }
    //修改表中的数据
    public  void updateAddress(ContentValues values,String WhereClause,String[] WhereArgs){//WhereClause：修改项查找的条件  WhereArgs：条件的名称
        try{
            db.update("tb_address",values,WhereClause,WhereArgs);
        }catch (Exception e){e.printStackTrace();}
    }
    //查询表中的数据
    public List<Address> selectAddress(String account_num){
        List<Address>addresses=new ArrayList<>();
        Address address;
        String sql="select * from tb_address where userPhone=?";
        try{
            Cursor cursor=db.rawQuery(sql,new String[]{account_num});
            if(cursor!=null){
                while (cursor.moveToNext()){
                    int id=cursor.getInt(cursor.getColumnIndex("address_id"));
                    String account_numStr=cursor.getString(cursor.getColumnIndex("userPhone"));
                    String username=cursor.getString(cursor.getColumnIndex("username"));
                    String user_phone=cursor.getString(cursor.getColumnIndex("user_phone"));
                    String area=cursor.getString(cursor.getColumnIndex("area"));
                    String address_remark=cursor.getString(cursor.getColumnIndex("address_remark"));
                    address=new Address(id,account_numStr,username,user_phone,area,address_remark);
                    addresses.add(address);
                }
            }
        }catch (Exception e){e.printStackTrace();}
      return  addresses;

    }

    public Address selectOneAddress(String address_id){
        Address address=new Address();
        String sql="select * from tb_address where address_id=?";
        try{
            Cursor cursor=db.rawQuery(sql,new String[]{address_id});
            if(cursor!=null){
                while (cursor.moveToNext()){
                    int id=cursor.getInt(cursor.getColumnIndex("address_id"));
                    String account_numStr=cursor.getString(cursor.getColumnIndex("userPhone"));
                    String username=cursor.getString(cursor.getColumnIndex("username"));
                    String user_phone=cursor.getString(cursor.getColumnIndex("user_phone"));
                    String area=cursor.getString(cursor.getColumnIndex("area"));
                    String address_remark=cursor.getString(cursor.getColumnIndex("address_remark"));
                    address=new Address(id,account_numStr,username,user_phone,area,address_remark);
                }
            }
        }catch (Exception e){e.printStackTrace();}
        return address;
    }
}

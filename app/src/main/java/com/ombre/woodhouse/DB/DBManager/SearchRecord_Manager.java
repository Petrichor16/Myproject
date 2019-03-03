package com.ombre.woodhouse.DB.DBManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ombre.woodhouse.DB.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMBRE on 2017/12/4.
 */

//表管理
public class SearchRecord_Manager {
    private final static int VERSION=1;//数据库版本
    private final static  String DB_NAME="WHshopping.db";//数据库名
    private DBHelper dbHelper;//创建一个数据库对象
    private SQLiteDatabase db;//创建一个SQLiteDatabase对象，用户实现增删查改的操作
    public SearchRecord_Manager(Context context) {
        try{dbHelper = new DBHelper(context,DB_NAME,null,VERSION);
            db = dbHelper.getWritableDatabase();}catch (Exception e){
            e.printStackTrace();
        }
    }
    //添加信息至表中
    public  void insertSearchRecord(ContentValues values,String content){
        try{
            String sql = "select * from tb_searchrecord  where  search_content=?";
            Cursor cursor=db.rawQuery(sql,new String[]{ String.valueOf(content)});
            if (!cursor.moveToNext())
                  db.insert("tb_searchrecord",null,values);
            db.close();
        }catch (Exception e){e.printStackTrace();}

    }
    //从表中删除数据
    public void deleteSearchRecord(){
        try{
            db.delete("tb_searchrecord",null,null);
        }catch (Exception e){e.printStackTrace();}
    }
    //修改表中的数据
    public  void updateSearchRecord(ContentValues values,String WhereClause,String WhereArgs){//WhereClause：修改项查找的条件  WhereArgs：条件的名称

        try{
            db.update("tb_searchrecord",values,WhereClause,new String[]{WhereArgs});
        }catch (Exception e){e.printStackTrace();}
    }

    //查询表中的数据
    public List<String> selectRecord(){
        List<String> stringList=new ArrayList<>();

        try{
            Cursor cursor=db.query("tb_searchrecord",null,null,null,null,null,null);
            if(cursor!=null){
                while (cursor.moveToNext()){
                    String str=cursor.getString(cursor.getColumnIndex("search_content"));
                    stringList.add(str);
                }
            }
        }catch (Exception e){e.printStackTrace();}

        return stringList;
    }
}

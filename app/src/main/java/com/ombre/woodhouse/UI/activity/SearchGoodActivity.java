package com.ombre.woodhouse.UI.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ombre.woodhouse.Adapter.Comm_CardAdapter;
import com.ombre.woodhouse.Adapter.SearchRecordAdapter;
import com.ombre.woodhouse.Bean.Commodity;
import com.ombre.woodhouse.Bean.Item.CommCarditem;
import com.ombre.woodhouse.DB.DBManager.Address_Manager;
import com.ombre.woodhouse.DB.DBManager.Footprint_Manager;
import com.ombre.woodhouse.DB.DBManager.SearchRecord_Manager;
import com.ombre.woodhouse.DialoaFile.Loading_Dialog;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.CollectInterfaceService;
import com.ombre.woodhouse.InterfaceService.CommodityInterfaceService;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

//搜索商品
public class SearchGoodActivity extends AppCompatActivity {

    private TextView btn_search;//搜索按钮
    private EditText edit_search;//搜索内容
    private RecyclerView search_result;//搜索结果显示
    private RecyclerView recordList;//搜索记录显示
    private TextView no_searchgoods;//没有搜索到商品
    private LinearLayout search_record;//搜索历史
    private TextView clear_editText;//清空编辑框
    List<String> mData;
    SearchRecordAdapter myAdapter;
    Loading_Dialog dialog;
    List<Commodity> commodityList;
    List<CommCarditem> commCarditemList;
    private void init(){
        btn_search=(TextView)findViewById(R.id.btn_search);
        edit_search=(EditText)findViewById(R.id.edit_search);
        search_result=(RecyclerView)findViewById(R.id.search_result);
        recordList=(RecyclerView)findViewById(R.id.recordList);
        no_searchgoods=(TextView)findViewById(R.id.no_searchgoods);
        search_record=(LinearLayout)findViewById(R.id.search_record);
        clear_editText=(TextView)findViewById(R.id.clear_editText);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_good);
        init();
        dialog=new Loading_Dialog(this,R.style.dialog);
        loadRecord();
        edit_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_search.setText("搜索");
            }
        });
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(edit_search.getText().toString().equals(""))
                    clear_editText.setVisibility(View.INVISIBLE);
                else
                    clear_editText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edit_search.getText().toString().equals(""))
                    clear_editText.setVisibility(View.INVISIBLE);
                else
                    clear_editText.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edit_search.getText().toString().equals(""))
                    clear_editText.setVisibility(View.INVISIBLE);
                else
                    clear_editText.setVisibility(View.VISIBLE);
            }
        });
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_search://搜索
                String txt=btn_search.getText().toString();
                if (txt.equals("搜索")){
                    saveRecord();

                }else{
                    dialog.dismiss();
                    btn_search.setText("搜索");
                    search_record.setVisibility(View.VISIBLE);
                    no_searchgoods.setVisibility(View.INVISIBLE);
                    search_result.setVisibility(View.INVISIBLE);
                    loadRecord();
                }

                break;
            case R.id.deleteRecord://删除记录
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog alertDialog;
                alertDialog= builder.setTitle("提示")
                        .setMessage("确定清空所有搜索记录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new SearchRecord_Manager(SearchGoodActivity.this).deleteSearchRecord();
                                mData.clear();
                                recordList.removeAllViews();
                                myAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("取消",null).create();
                alertDialog.show();
                break;
            case R.id.clear_editText:
                edit_search.setText("");
                break;
        }
    }
    //获取搜索记录
     private void loadRecord(){
         mData=new SearchRecord_Manager(this).selectRecord();
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
         recordList.setLayoutManager(layoutManager);
         Collections.reverse(mData);
         myAdapter=new SearchRecordAdapter(this,mData);
         myAdapter.setOnItemClickListener(new SearchRecordAdapter.OnItemClickListener() {
             @Override
             public void onItemClick(View view, int position) {
                 edit_search.setText(mData.get(position));
                 saveRecord();
                 btn_search.setText("取消");
                 search_record.setVisibility(View.INVISIBLE);
             }
         });
         recordList.setAdapter(myAdapter);
     }

    //保存搜索记录
    private void saveRecord(){
        String SearchStr=edit_search.getText().toString();
        if(SearchStr.equals(""))
            Toast.makeText(getApplicationContext(),"请输入你要搜索的关键字",Toast.LENGTH_SHORT).show();
        else
        {
            btn_search.setText("取消");
            search_record.setVisibility(View.INVISIBLE);
            ContentValues values = new ContentValues();
            values.put("userPhone","" );
            values.put("search_content", SearchStr);
            new SearchRecord_Manager(SearchGoodActivity.this).insertSearchRecord(values,SearchStr);
            values.clear();

            String Str="%";
            for (int i=0;i<SearchStr.length();i++){
                Str=Str+SearchStr.substring(i,i+1)+"%";
            }
            getSearchResult(Str);
        }
    }

    //获取搜索结果数据
    private void getSearchResult(final String content){
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Map<String, String> map = new HashMap<String, String>();
                map.put("searchContent", content);
                CommodityInterfaceService service = new CommodityInterfaceService(SearchGoodActivity.this);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(),"网络不给力",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result=response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                commodityList=new analysisXMLHelper().analysisAllCommodity(result);//获取对象
                                if(commodityList!=null) {
                                    loadGoodsList();
                                    no_searchgoods.setVisibility(View.INVISIBLE);
                                    search_result.setVisibility(View.VISIBLE);
                                }else {
                                    no_searchgoods.setVisibility(View.VISIBLE);
                                    search_result.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    }
                };
                service.getSearchGoods(map,callbacks);
            }
        }).start();
    }

    //加载商品
    private void loadGoodsList(){
        commCarditemList =new ArrayList<>();
        CommCarditem commCardItem;
        for(int i=0;i<commodityList.size();i++){
            Commodity  comm=commodityList.get(i);
            commCardItem=new CommCarditem(comm.getId(),comm.getPicture()
                    ,comm.getStatus(),comm.getMerName(),comm.getPrice(),comm.getSale_value());
            commCarditemList.add(commCardItem);
        }
        StaggeredGridLayoutManager gridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        search_result.setLayoutManager(gridLayoutManager);
        Comm_CardAdapter  myAdapter=new Comm_CardAdapter(this,commCarditemList);
        search_result.setAdapter(myAdapter);
    }
}

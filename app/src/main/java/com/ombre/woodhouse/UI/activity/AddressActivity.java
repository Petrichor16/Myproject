package com.ombre.woodhouse.UI.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.ombre.woodhouse.Adapter.AddressAdapter;
import com.ombre.woodhouse.Bean.Address;
import com.ombre.woodhouse.DB.DBManager.Address_Manager;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;

import java.util.List;

//我的地址
public class AddressActivity extends AppCompatActivity {

    private RecyclerView myAddressList;
    Address_Manager address_manager;
    List<Address> address_List;
    AddressAdapter address_cardAdapter;

    String type;
    private void init(){
        myAddressList=(RecyclerView)findViewById(R.id.myAddressList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Intent intent_get=getIntent();
        type=intent_get.getStringExtra("type");
        init();
        getAddress();
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.address_back:
                finish();
                break;
            case R.id.addnewAddress://添加新地址
                Intent intent=new Intent(AddressActivity.this,AddNewAddressActivity.class);
                intent.putExtra("type","add");
                intent.putExtra("address_id","");
                startActivity(intent);
                break;
        }
    }
    //获得地址
    private void getAddress(){
        address_manager=new Address_Manager(this);
        address_List=address_manager.selectAddress(new SharePreferences_getData(this).getUserID());
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        myAddressList.setLayoutManager(layoutManager);
        address_cardAdapter = new AddressAdapter(this,address_List);
        address_cardAdapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(type.equals("order")){
                    new SharePreferences_Manager(AddressActivity.this).saveAddressID(String.valueOf(address_List.get(position).getId()));
                    finish();
                }
            }
        });
        myAddressList.setAdapter(address_cardAdapter);
    }

    @Override
    protected void onResume() {
        getAddress();
        super.onResume();
    }
}

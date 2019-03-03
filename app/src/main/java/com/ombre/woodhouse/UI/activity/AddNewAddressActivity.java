package com.ombre.woodhouse.UI.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ombre.woodhouse.Bean.Address;
import com.ombre.woodhouse.DB.DBManager.Address_Manager;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;

import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.OnAddressSelectedListener;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;

public class AddNewAddressActivity extends AppCompatActivity {

    private EditText consignee;//收货人
    private EditText Contact_number;//电话
    private EditText area;//地区
    private EditText remark;//备注
    private TextView titleName;
    private TextView btn_Preservation;
    String consigneeStr;
    String Contact_numberStr;//电话
    String areaStr;//地区
    String remarkStr;//备注
    String type;
    String address_id;
    Address address;


    BottomDialog dialog;//地址选择器
    private void init() {
        consignee = (EditText) findViewById(R.id.consignee);//收货人
        Contact_number = (EditText) findViewById(R.id.Contact_number);//电话
        area = (EditText) findViewById(R.id.area);//地区
        remark = (EditText) findViewById(R.id.address_remark);//备注
        titleName = (TextView) findViewById(R.id.titleName);
        btn_Preservation = (TextView) findViewById(R.id.btn_Preservation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
        init();
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        address_id = intent.getStringExtra("address_id");
        if (type.equals("edit")) {
            address = new Address_Manager(this).selectOneAddress(address_id);
            if (address != null) {
                consignee.setText(address.getUsername());//收货人
                Contact_number.setText(address.getUser_phone());//电话
                area.setText(address.getArea());//地区
                remark.setText(address.getAddress_remark());//备注
                titleName.setText("编辑地址");
            }
        } else {
            titleName.setText("添加新地址");
        }

        //地址选择
        area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog  = new BottomDialog(AddNewAddressActivity.this);
                dialog.setOnAddressSelectedListener(new OnAddressSelectedListener() {
                    @Override
                    public void onAddressSelected(Province province, City city, County county, Street street) {
                        String str=(province == null ? "" : province.name) +
                                (city == null ? "" : "" + city.name) +
                                (county == null ? "" : "" + county.name) +
                                (street == null ? "" : "" + street.name);
                        area.setText(str);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addnewaddress_back:
                finish();
                break;
            case R.id.btn_Preservation://添加新地址
                if (type.equals("add"))
                    PreservationAddress();
                else
                    updateAddress();
                    break;
        }
    }


    //添加新地址
    private void PreservationAddress() {
        consigneeStr = consignee.getText().toString();//收货人
        Contact_numberStr = Contact_number.getText().toString();//电话
        areaStr = area.getText().toString();//地区
        remarkStr = remark.getText().toString();//备注
        if (consigneeStr.equals("") || Contact_numberStr.equals("") || areaStr.equals("")) {
            Toast.makeText(getApplicationContext(), "请将收货人、电话、地区填写完整！", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues values = new ContentValues();
            values.put("userPhone", new SharePreferences_getData(AddNewAddressActivity.this).getUserID());
            values.put("username", consigneeStr);
            values.put("user_phone", Contact_numberStr);
            values.put("area", areaStr);
            values.put("address_remark", remarkStr);
            new Address_Manager(AddNewAddressActivity.this).insertAddress(values);
            values.clear();
            Toast.makeText(getApplicationContext(), "保存成功！", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    //修改地址
    private void updateAddress() {
    consigneeStr=consignee.getText().toString();//收货人
    Contact_numberStr=Contact_number.getText().toString();//电话
    areaStr=area.getText().toString();//地区
    remarkStr =remark.getText().toString();//备注
   if(consigneeStr.equals("")&&Contact_numberStr.equals("")&&areaStr.equals(""))
    {
        Toast.makeText(getApplicationContext(), "请将收货人、电话、地区填写完整！", Toast.LENGTH_SHORT).show();
    } else {
        ContentValues values = new ContentValues();
        values.put("username", consigneeStr);
        values.put("user_phone", Contact_numberStr);
        values.put("area", areaStr);
        values.put("address_remark", remarkStr);
        new Address_Manager(this).updateAddress(values, "address_id=?",
                new String[]{address_id});
        values.clear();
        Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_SHORT).show();
       finish();
    }
}
}

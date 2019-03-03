package com.ombre.woodhouse.UI.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ombre.woodhouse.DialoaFile.Loading_Dialog;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.EvaluationInterfaceService;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

//显示商品评价
public class EvaluationGoodsActivity extends AppCompatActivity {

    private TextView eva_orderNum;//订单编号
    private EditText  eva_content;//评论内容
    private RatingBar eva_startLevel;//星级

    Loading_Dialog dialog;
    String orderID;
    String orderNum;
    private void init(){
        eva_orderNum=(TextView)findViewById(R.id.eva_orderNum);
        eva_content=(EditText)findViewById(R.id.eva_content);
        eva_startLevel=(RatingBar)findViewById(R.id.eva_startLevel);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_goods);
        init();
        dialog=new Loading_Dialog(this,R.style.dialog);
        Intent intent=getIntent();
        orderID=intent.getStringExtra("orderID");
        orderNum=intent.getStringExtra("orderNum");
        eva_orderNum.setText(orderNum);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.goodEva_back:
                finish();
                break;
            case R.id.submit_eva:
                if (!eva_content.getText().toString().equals(""))
                    submitEvaluation();
                else
                    Toast.makeText(getApplicationContext(),"请填写评价内容",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //提交评价
    private void submitEvaluation(){
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("orderID",orderID);
                map.put("eva_content",eva_content.getText().toString());
                map.put("eva_startLevel", String.valueOf(eva_startLevel.getRating()));
                map.put("memberPhone",new SharePreferences_getData(EvaluationGoodsActivity.this).getUserID());
                EvaluationInterfaceService service=new EvaluationInterfaceService(EvaluationGoodsActivity.this);
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                finish();
                            }
                        });
                    }
                };
                service.submitEva(map,callbacks);
            }
        }).start();
    }
}

package com.ombre.woodhouse.UI.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.ombre.woodhouse.Adapter.EvaluationAdapter;
import com.ombre.woodhouse.Bean.Evaluation;
import com.ombre.woodhouse.DialoaFile.Loading_Dialog;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.EvaluationInterfaceService;
import com.ombre.woodhouse.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class AllEvaluationActivity extends AppCompatActivity {

    private RecyclerView AllEvaluation;
    private SwipeRefreshLayout evaluationRefresh;
    String goodsId;
    List<Evaluation> evaluationList;
    Loading_Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_evaluation);
        AllEvaluation=(RecyclerView)findViewById(R.id.AllEvaluation);
        evaluationRefresh=(SwipeRefreshLayout)findViewById(R.id.evaluationRefresh);
        dialog=new Loading_Dialog(this,R.style.dialog);
        dialog.show();

        Intent intent=getIntent();
        goodsId=intent.getStringExtra("goodsId");
        getEvaluationConnect();
        evaluationRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEvaluationConnect();
            }
        });
    }

    public void onClick(View view){
        finish();
    }
    //获取评论
    private void getEvaluationConnect(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("goodsId",goodsId);
                EvaluationInterfaceService service=new EvaluationInterfaceService(AllEvaluationActivity.this);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                evaluationRefresh.setRefreshing(false);
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
                               evaluationRefresh.setRefreshing(false);
                               evaluationList=new analysisXMLHelper().analysisGoodsEvaluation(result);
                               loadEvaluation();
                           }
                       });
                    }
                };
                service.getEvaluation(map,callbacks);
            }
        }).start();
    }

    //加载评论
    private void loadEvaluation(){
        StaggeredGridLayoutManager linearLayoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        AllEvaluation.setLayoutManager(linearLayoutManager);
        EvaluationAdapter myAdapter=new EvaluationAdapter(this,evaluationList);
        AllEvaluation.setAdapter(myAdapter);
    }
}

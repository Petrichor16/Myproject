package com.ombre.woodhouse.UI.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.ombre.woodhouse.Adapter.ParametersAdapter;
import com.ombre.woodhouse.Adapter.PictureDisplayAdapter;
import com.ombre.woodhouse.Adapter.SowingmapAdapter;
import com.ombre.woodhouse.Bean.Commodity;
import com.ombre.woodhouse.Bean.Evaluation;
import com.ombre.woodhouse.Bean.Param;
import com.ombre.woodhouse.Bean.Picture;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.CartInterfaceServicce;
import com.ombre.woodhouse.InterfaceService.CollectInterfaceService;
import com.ombre.woodhouse.InterfaceService.CommodityInterfaceService;
import com.ombre.woodhouse.InterfaceService.EvaluationInterfaceService;
import com.ombre.woodhouse.InterfaceService.GetPicture;
import com.ombre.woodhouse.InterfaceService.ParamInterfaceService;
import com.ombre.woodhouse.MyView.AlphaTitleScrollView;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;
import com.ombre.woodhouse.Utils.IconColor_Change;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;

public class CommoditydetailsAcitivity extends AppCompatActivity {

    private ViewPager comm_details_sowingPic;//详细信息轮播图
    private AlphaTitleScrollView commdetails_scrollview;
    private TextView text_commdetails;//详细信息
    private LinearLayout commdetails_title;//标题栏
    private LinearLayout commdetails_dot;
    private ImageView btn_collect;//收藏
    private TextView add_cart;//加入购物车
    private TextView btn_buy;//购买
    private TextView detailed_name;//商品名称
    private TextView detailed_price;//商品价格
    private TextView detailed_stock;//剩余
    private TextView detailed_salenum;//已销售
    private LinearLayout btn_evaluation;//查看评价
    private TextView goodsDetails_userId;//评价栏的用户名
    private TextView goodsDetails_evalContent;//评价内容
    private TextView no_evaluation;//没有评论
    private List<Evaluation> evaluationList;//评价列表
    private RecyclerView goods_parameters;//商品参数
    private List<Param> paramList;//参数列表
    private RecyclerView goods_detailsPic;//商品详情展示
    List<String> pic_detailsList;
    private RecyclerView Physical_display;//s商品实物展示
    List<String> pic_physicalList;
    List<String> pic_sowingList;//轮播图
    private TextView values_sub;//'-
    private EditText edit_values;//'购买量
    private TextView values_add;//'+
   private SpinKitView detailsLoading;
    SharePreferences_getData preferences_getData;
    private boolean login_state;//登录状态
    Intent intent_toLodin;
    private Timer timer;
    int prePosition=0;
    String  goodsId;
    Commodity commodity;
    List<Picture> pictureList;
    public static final int GOODSDETAILSCONNECT_FAILS=3306;//网络连接成功
    public static final int GOODSDETAILSSUCCESS=3307;//获取详细信息成功
    public static final int GOODSPARAMETERSSUCCESS=3310;//获取参数成功
    public static final int GOODSEVALUATIONSSUCCESS=3311;//评论获取成功
    public static final int GOODSGETPICTURESSUCCESS=3312;//评论图片成功
    public static final int COLLECTCONNECTSUCCESS=3313;//收藏操作成功
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GOODSDETAILSCONNECT_FAILS://网络不能支持登录
                    detailsLoading.setVisibility(View.INVISIBLE);
                    Toast.makeText(CommoditydetailsAcitivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    break;
                case GOODSDETAILSSUCCESS://验证成功后
                    detailsLoading.setVisibility(View.INVISIBLE);
                    commodity=new analysisXMLHelper().analysisCommodityDetails(msg.obj.toString());
                    if(commodity!=null)
                    {
                        getCartNumber();
                        loadGoodsDetails();
                    }

                    break;
                case GOODSPARAMETERSSUCCESS://验证成功后
                    paramList=new ArrayList<>();
                    paramList=new analysisXMLHelper().analysisGoodsParam(msg.obj.toString());
                    if(paramList!=null)
                        loadParam();
                case GOODSEVALUATIONSSUCCESS://验证成功后
                    evaluationList=new analysisXMLHelper().analysisGoodsEvaluation(msg.obj.toString());
                    if(evaluationList!=null&&evaluationList.size()>0)
                    {
                            btn_evaluation.setVisibility(View.VISIBLE);
                            goodsDetails_userId.setText(evaluationList.get(0).getUserName());
                            goodsDetails_evalContent.setText(evaluationList.get(0).getContent());
                            no_evaluation.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        no_evaluation.setVisibility(View.VISIBLE);
                        btn_evaluation.setVisibility(View.INVISIBLE);}
                    getPicturePath();
                    break;
                case GOODSGETPICTURESSUCCESS:
                    pictureList=new analysisXMLHelper().analysisGoodsPic(msg.obj.toString());
                    if(pictureList!=null)
                        loadPicture();
                    break;
                case COLLECTCONNECTSUCCESS:
                   if(msg.obj.toString().equals("true"))
                       new IconColor_Change().setImageViewColor(btn_collect,R.color.colorBlack);
                   else
                       new IconColor_Change().setImageViewColor(btn_collect,R.color.colorGray);
                    break;
            }
        }
    };

    private void initViews(){
        comm_details_sowingPic=(ViewPager)findViewById(R.id.comm_details_sowingPic);
        commdetails_scrollview=(AlphaTitleScrollView)findViewById(R.id.commdetails_scrollview);
        text_commdetails=(TextView)findViewById(R.id.text_commdetails);
        commdetails_title=(LinearLayout)findViewById(R.id.commdetails_title);
        commdetails_dot=(LinearLayout)findViewById(R.id.commdetails_dot);
        add_cart=(TextView)findViewById(R.id.add_cart);//加入购物车
        btn_buy=(TextView)findViewById(R.id.btn_buy);//购买
        btn_collect=(ImageView)findViewById(R.id.btn_collect);
        detailed_name=(TextView)findViewById(R.id.detailed_name);
        detailed_price=(TextView)findViewById(R.id.detailed_price);
        detailed_stock=(TextView)findViewById(R.id.detailed_stock);
        detailed_salenum=(TextView)findViewById(R.id.detailed_salenum);
        goodsDetails_userId=(TextView)findViewById(R.id.goodsDetails_userId);
        goodsDetails_evalContent=(TextView)findViewById(R.id.goodsDetails_evalContent);
        btn_evaluation=(LinearLayout)findViewById(R.id.btn_evaluation);
        goods_parameters=(RecyclerView) findViewById(R.id.goods_parameters);
        goods_detailsPic=(RecyclerView)findViewById(R.id.goods_detailsPic);
        Physical_display=(RecyclerView)findViewById(R.id.Physical_display);
        no_evaluation=(TextView)findViewById(R.id.no_evaluation);
        values_sub=(TextView)findViewById(R.id.values_sub);//'-
        edit_values=(EditText)findViewById(R.id.edit_values);//'购买量
        values_add=(TextView)findViewById(R.id.values_add);//'+

        detailsLoading=(SpinKitView)findViewById(R.id.detailsLoading);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commoditydetails_acitivity);
        initViews();
        preferences_getData=new SharePreferences_getData(this);
        login_state=preferences_getData.getLoginState();
         intent_toLodin=new Intent(this,LoginActivity.class);
        Intent in=getIntent();
        goodsId=in.getStringExtra("goodsId");
        Physical_display.setHasFixedSize(true);
        Physical_display.setNestedScrollingEnabled(false); //解决Scrollview与recycleview嵌套后滑动卡顿的问题
        goods_detailsPic.setHasFixedSize(true);
        goods_detailsPic.setNestedScrollingEnabled(false); //解决Scrollview与recycleview嵌套后滑动卡顿的问题
        goods_parameters.setHasFixedSize(true);
        goods_parameters.setNestedScrollingEnabled(false); //解决Scrollview与recycleview嵌套后滑动卡顿的问题
        initTitleContent();
        getGoodsDetailsConnect();
        getParamConnect();
        CollectOperationt("1");
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.commdetails_back://返回
                finish();
                break;
            case R.id.btn_buy://购买
                if(login_state){
                    Intent intent_g=new Intent(this,GeneratingorderActivity.class);
                    intent_g.putExtra("goodsId",goodsId);
                    intent_g.putExtra("values",edit_values.getText().toString());
                    startActivity(intent_g);
                }else{
                    startActivity(intent_toLodin);
                }
                break;
            case R.id.add_cart://加入购物车
                if(login_state){
                    detailsLoading.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            addCart();
                        }
                    },0);
                }else{
                    startActivity(intent_toLodin);
                }
                break;
            case R.id.btn_collect://收藏
                if(login_state){
                    CollectOperationt("2");
                }else{
                    startActivity(intent_toLodin);
                }
                break;
            case  R.id.btn_evaluation://商品评价
                Intent intent_eva=new Intent(this,AllEvaluationActivity.class);
                intent_eva.putExtra("goodsId",goodsId);
                startActivity(intent_eva);
                break;
            case R.id.values_sub:
                int value=Integer.valueOf(edit_values.getText().toString());
                if(value>1)
                { value--;
                    values_sub.setBackgroundResource(R.drawable.edittextbg_3);}
                else
                    values_sub.setBackgroundResource(R.drawable.edittextbg_2);
                edit_values.setText(""+value);
                break;
            case R.id.values_add:
                int value1=Integer.valueOf(edit_values.getText().toString());
                    value1++;
                if(value1>1)
                    values_sub.setBackgroundResource(R.drawable.edittextbg_3);
                edit_values.setText(""+value1);
                break;

        }
    }

    //商品收藏操作
    private void CollectOperationt(final String flag){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("goodsId",goodsId);
                map.put("userPhone",preferences_getData.getUserID());
                map.put("flag",flag);
                CollectInterfaceService service=new CollectInterfaceService(CommoditydetailsAcitivity.this);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {}
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result=response.body().string();
                        Message msg=new Message();
                        msg.what=COLLECTCONNECTSUCCESS;
                        msg.obj=result;
                        mHandler.sendMessage(msg);
                    }
                };
                service.CollectOperationtype(map,callbacks);
            }
        }).start();
    }
    //连接服务器获取商品
    private void getGoodsDetailsConnect(){
        detailsLoading.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("goodsId",goodsId);
                CommodityInterfaceService service=new CommodityInterfaceService(CommoditydetailsAcitivity.this);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Message message = new Message();
                        message.what = GOODSDETAILSCONNECT_FAILS;
                        mHandler.sendMessage(message);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result=response.body().string();
                        Message msg=new Message();
                        msg.what=GOODSDETAILSSUCCESS;
                        msg.obj=result;
                        mHandler.sendMessage(msg);
                    }
                };
                service.getCommdityDetails(map,callbacks);
            }
        }).start();
    }
    //初始化标题栏状态
    private void initTitleContent(){
        commdetails_scrollview.setTitleAndHead(commdetails_title,text_commdetails, comm_details_sowingPic,0);
        commdetails_title.getBackground().setAlpha(0);
        text_commdetails.setTextColor(text_commdetails.getTextColors().withAlpha(0));
    }
    //实现图片轮播
    private void realizeSowingmap(){
        initDots();
        comm_details_sowingPic.setPageMargin(10);//设置页与页之间的间距
        comm_details_sowingPic.setOffscreenPageLimit(1);//限定预加载的页面个数
        SowingmapAdapter myAdapter=new SowingmapAdapter(CommoditydetailsAcitivity.this,pic_sowingList);
        myAdapter.setOnItemClickListener(new SowingmapAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(CommoditydetailsAcitivity.this,PicExhibitionActivity.class);
                new SharePreferences_Manager(CommoditydetailsAcitivity.this).savePicPath(pic_sowingList.get(position%pic_sowingList.size()));
                startActivity(intent);
            }
        });
        comm_details_sowingPic.setAdapter(myAdapter);
        comm_details_sowingPic.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                commdetails_dot.getChildAt(prePosition).setEnabled(false);
                commdetails_dot.getChildAt(position % pic_sowingList.size()).setEnabled(true);
                prePosition = position % pic_sowingList.size();
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        if (null != timer) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        comm_details_sowingPic.setCurrentItem(comm_details_sowingPic.getCurrentItem()+1);//用来制定初始化的页面
                    }
                });
            }
        }, 0, 3000);
    }
    //添加轮播栏的圆点
    private void initDots() {
        if (null != commdetails_dot) {
            commdetails_dot.removeAllViews();//清除所有的布局控件
        }
        for (int i = 0; i < pic_sowingList.size(); i++) {
            ImageView dot = new ImageView(this);
            dot.setEnabled(false);
            dot.setImageResource(R.drawable.dot);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 15;
            dot.setLayoutParams(params);
            commdetails_dot.addView(dot);
        }
        commdetails_dot.getChildAt(0).setEnabled(true);
    }
    //加载商品信息
    private void loadGoodsDetails(){
        detailed_name.setText(commodity.getMerName());
        detailed_price.setText("¥ "+commodity.getPrice());
        if(commodity.getSale_value()!=null)
            detailed_salenum.setText("已售出"+commodity.getSale_value()+"件");
        else
            detailed_salenum.setText("已售出0件");
        detailed_stock.setText(String.valueOf(commodity.getStock()));

    }
    //获取图片地址
    private void  getPicturePath(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("goodsId",goodsId);
                GetPicture service=new GetPicture(CommoditydetailsAcitivity.this,null);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result1=response.body().string();
                       Message message=new Message();
                        message.what=GOODSGETPICTURESSUCCESS;
                        message.obj=result1;
                       mHandler.sendMessage(message);
                    }
                };
                service.getPicPath(map,callbacks);
            }
        }).start();
    }
    //加载图片
    private void loadPicture(){
        pic_detailsList=new ArrayList<>();
        pic_physicalList=new ArrayList<>();
        pic_sowingList=new ArrayList<>();
        if(pictureList.size()>0) {
            for (int i = 0; i < pictureList.size(); i++) {
                Picture picture = pictureList.get(i);
                if (picture.getPictureType() == 1)//轮播图
                {
                    GetPicture getPicture=new GetPicture();
                    List<String> picList=getPicture.picPath(picture.getPicturePath());
                    pic_sowingList.addAll(picList);
                }
                if (picture.getPictureType() == 3)//详情
                {
                    GetPicture getPicture=new GetPicture();
                    List<String> picList=getPicture.picPath(picture.getPicturePath());
                    pic_detailsList.addAll(picList);
                }
                  //  pic_detailsList.add(picture.getPicturePath());
                if (picture.getPictureType() == 4)//实物展示
                { GetPicture getPicture=new GetPicture();
                    List<String> picList=getPicture.picPath(picture.getPicturePath());
                    pic_physicalList.addAll(picList);
                }
                   // pic_physicalList.add(picture.getPicturePath());
            }
        }

        StaggeredGridLayoutManager layoutManager;
        PictureDisplayAdapter myAdapter;
        if(pic_physicalList!=null){
            layoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
            Physical_display.setLayoutManager(layoutManager);
            myAdapter=new PictureDisplayAdapter(this,pic_physicalList);
            myAdapter.setOnItemClickListener(new PictureDisplayAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent1=new Intent(CommoditydetailsAcitivity.this,PicExhibitionActivity.class);
                    new SharePreferences_Manager(CommoditydetailsAcitivity.this).savePicPath(pic_physicalList.get(position));
                    startActivity(intent1);
                }
            });
            Physical_display.setAdapter(myAdapter);
        }

        if(pic_detailsList!=null){
            layoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
            goods_detailsPic.setLayoutManager(layoutManager);
            myAdapter=new PictureDisplayAdapter(this,pic_detailsList);
            myAdapter.setOnItemClickListener(new PictureDisplayAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent2=new Intent(CommoditydetailsAcitivity.this,PicExhibitionActivity.class);
                    new SharePreferences_Manager(CommoditydetailsAcitivity.this).savePicPath(pic_detailsList.get(position));
                    startActivity(intent2);
                }
            });
            goods_detailsPic.setAdapter(myAdapter);
        }
        if(pic_sowingList!=null&&pic_sowingList.size()>0){
            realizeSowingmap();
        }
    }
    //获取参数
    private void getParamConnect(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("goodsId",goodsId);
                ParamInterfaceService service=new ParamInterfaceService(CommoditydetailsAcitivity.this);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result=response.body().string();
                        Message msg=new Message();
                        msg.what=GOODSPARAMETERSSUCCESS;
                        msg.obj=result;
                        mHandler.sendMessage(msg);
                    }
                };
                service.getParam(map,callbacks);
            }
        }).start();
    }
    //加载参数
    private void loadParam(){
        if(paramList.size()>0){
            StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
            goods_parameters.setLayoutManager(layoutManager);
            ParametersAdapter myAdapter=new ParametersAdapter(this,paramList);
            goods_parameters.setAdapter(myAdapter);
        }
        getEvaluationConnect();
    }
    //获取评论
    private void getEvaluationConnect(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("goodsId",goodsId);
                EvaluationInterfaceService service=new EvaluationInterfaceService(CommoditydetailsAcitivity.this);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result=response.body().string();
                        Message msg=new Message();
                        msg.what=GOODSEVALUATIONSSUCCESS;
                        msg.obj=result;
                        mHandler.sendMessage(msg);
                    }
                };
                service.getEvaluation(map,callbacks);
            }
        }).start();
    }
    //加入购物车
    private void addCart(){
                Map<String, String> map = new HashMap<String, String>();
                map.put("goodsID", goodsId);
                map.put("memberPhone",preferences_getData.getUserID());
                map.put("number",edit_values.getText().toString());
                CartInterfaceServicce service=new CartInterfaceServicce(CommoditydetailsAcitivity.this);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            detailsLoading.setVisibility(View.INVISIBLE);
                            Toast.makeText(CommoditydetailsAcitivity.this,"网络不给力",Toast.LENGTH_SHORT).show();
                        }
                    });}

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result=response.body().string();
                       runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                detailsLoading.setVisibility(View.INVISIBLE);
                                Toast.makeText(CommoditydetailsAcitivity.this,result,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                };
                service.addCart(map,callbacks);
    }
    //获取购物车中的数量
    private void getCartNumber(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("goodsId",goodsId);
                map.put("memberPhone",preferences_getData.getUserID());
                CommodityInterfaceService service=new CommodityInterfaceService(CommoditydetailsAcitivity.this);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result=response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                edit_values.setText(result);
                            }
                        });
                    }
                };
                service.getCartnumber(map,callbacks);
            }
        }).start();
    }
    @Override
    protected void onResume() {
        preferences_getData=new SharePreferences_getData(this);
        login_state=preferences_getData.getLoginState();
        getGoodsDetailsConnect();
        super.onResume();
    }
}

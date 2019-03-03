package com.ombre.woodhouse.UI.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ombre.woodhouse.InterfaceService.GetPicture;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;

public class PicExhibitionActivity extends AppCompatActivity {
   private ImageView pic_exhibition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_exhibition);
        pic_exhibition=(ImageView)findViewById(R.id.pic_exhibition);
        String picPath=new SharePreferences_getData(this).getPicPath();
       new GetPicture(PicExhibitionActivity.this,pic_exhibition).loadPicture(picPath);
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.pic_exhibition_back:
                finish();
                break;
        }
    }
}

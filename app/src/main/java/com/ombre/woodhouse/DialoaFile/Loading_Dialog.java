package com.ombre.woodhouse.DialoaFile;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.ombre.woodhouse.R;

/**
 * Created by OMBRE on 2018/5/9.
 */
//登录进度条
public class Loading_Dialog extends AlertDialog {

    public Loading_Dialog(Context context, int theme) {
        super(context, theme);
    }

    public Loading_Dialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_loading_dialog);
    }
}

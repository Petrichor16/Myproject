package com.ombre.woodhouse.Interface;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by OMBRE on 2018/4/28.
 */

public interface Callbacks {
    void onFailure(Call call, IOException e);//连接失败后的事件处理
    void onResponse(Call call, Response response) throws IOException;//连接成功后的时间处理
}

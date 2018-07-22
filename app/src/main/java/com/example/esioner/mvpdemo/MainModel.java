package com.example.esioner.mvpdemo;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainModel {
    /**
     * 根据 url 获取数据
     * @param url
     * @return
     */
    public Call getData(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        return client.newCall(request);
    }
}

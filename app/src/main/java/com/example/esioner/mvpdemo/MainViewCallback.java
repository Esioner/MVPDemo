package com.example.esioner.mvpdemo;

public interface MainViewCallback<T> {
     /**
      * 开始加载数据的时候页面的显示
      */
     void onStartLoading();
     /**
      * 正在加载的时候页面的显示逻辑
      */
     void onLoading(int progress);
     /**
      * 停止加载的时候页面显示的逻辑
      */
     void onStopLoading(T data);
     /**
      * 加载出错的时候页面的逻辑
      * @param e
      */
     void onLoadingError(Exception e);
}

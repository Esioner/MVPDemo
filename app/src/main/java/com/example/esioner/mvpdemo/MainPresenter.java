package com.example.esioner.mvpdemo;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainPresenter {
    private MainViewCallback viewCallback;
    private MainModel mainModel;

    /**
     * 构造方法
     * @param callback
     */
    public MainPresenter(MainViewCallback callback) {
        this.viewCallback = callback;
        this.mainModel = new MainModel();
    }

    /**
     * 加载数据
     *
     * @param url
     */
    public void loadData(String url) {
        viewCallback.onStartLoading();
        if (mainModel != null) {
            mainModel.getData(url).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    viewCallback.onLoadingError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    viewCallback.onStopLoading(responseBody);
                }
            });
        } else {
            viewCallback.onStopLoading(null);
        }
    }

    /**
     * 下载文件
     * @param url
     */
    public void downloadFile(String url) {
        viewCallback.onStartLoading();
        if (mainModel != null) {
            mainModel.getData(url).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    viewCallback.onLoadingError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    long length = response.body().contentLength();

                    InputStream input = response.body().byteStream();

                    File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "云烟成雨.mp3");
                    if (file.exists()) {
                        file.delete();
                    }
                    OutputStream output = new FileOutputStream(file);

                    byte[] bytes = new byte[1024 * 1024];
                    int len;
                    long hasDownload = 0;
                    int progress;
                    while ((len = input.read(bytes)) != -1) {
                        output.write(bytes, 0, len);
                        hasDownload += len;
                        progress = (int) (((double) hasDownload / (double) length) * 100.0);
                        Log.d("download", "onResponse: " + progress);
                        viewCallback.onLoading(progress);
                    }
                    viewCallback.onStopLoading(file.getAbsolutePath());
                }
            });
        }
    }
}

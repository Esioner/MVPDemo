package com.example.esioner.mvpdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MainViewCallback, View.OnClickListener {
    private String TAG = MainActivity.class.getSimpleName();
    private Button btnLoadData;
    private TextView tvLoadResult;
    private ProgressBar progressBar;
    private MainPresenter mPresenter;
    private EditText editInput;
    private Button btnDownload;
    private ProgressBar progressBarHor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter(this);

        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        btnLoadData = findViewById(R.id.btn_load_data);
        btnDownload = findViewById(R.id.btn_download);
        tvLoadResult = findViewById(R.id.tv_load_result);
        progressBar = findViewById(R.id.progressbar);
        progressBarHor = findViewById(R.id.progressbar_hor);
        editInput = findViewById(R.id.edit_input);

        btnLoadData.setOnClickListener(this);
        btnDownload.setOnClickListener(this);
    }

    /**
     * type == 0 : 表示使用圆形progress
     * type == 1 : 表示使用横向progress
     */
    int TYPE = 0;

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_load_data:
                TYPE = 0;
                editInput.setText("https://blog.csdn.net/u014551772/article/details/73274646");
                if (editInput != null && editInput.getText().toString().trim().length() == 0) {
                    return;
                }
                mPresenter.loadData(editInput.getText().toString().trim());
                break;
            case R.id.btn_download:
                TYPE = 1;
                //检查权限
                checkNeedPermission();
                editInput.setText("https://d11.baidupcs.com/file/e102b1c28a104aa54f07b16207fa5c18?bkt=p3-000078dcae2f580d3bf4ea807f96bcc1ec43&xcode=fab4a62839fa5f9bed87a375ad5ebba0281da113df38ddfc7790c526bc6c2981c736361233c829baf8e46bfad9a314329a7e3ac4ae9d7ad8&fid=2130917896-250528-851172408452839&time=1532252898&sign=FDTAXGERLQBHSK-DCb740ccc5511e5e8fedcff06b081203-jL7Sl%2FL6KXknoxxWmZbV0EJnT%2Fw%3D&to=d11&size=9713768&sta_dx=9713768&sta_cs=34726&sta_ft=mp3&sta_ct=5&sta_mt=5&fm2=MH%2CYangquan%2CAnywhere%2C%2Cshanghai%2Ccmnet&resv0=cdnback&resv1=0&vuk=282335&iv=-2&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=000078dcae2f580d3bf4ea807f96bcc1ec43&sl=82640974&expires=8h&rt=sh&r=856987947&mlogid=90470550526066554&vbdid=4018271958&fin=%E4%BA%91%E7%83%9F%E6%88%90%E9%9B%A8+%E6%88%BF%E4%B8%9C%E7%9A%84%E7%8C%AB.mp3&fn=%E4%BA%91%E7%83%9F%E6%88%90%E9%9B%A8+%E6%88%BF%E4%B8%9C%E7%9A%84%E7%8C%AB.mp3&rtype=1&dp-logid=90470550526066554&dp-callid=0.1.1&hps=1&tsl=50&csl=78&csign=0vnYzTYv2VV%2Ff%2FRkrbacf8q2JPs%3D&so=0&ut=8&uter=4&serv=0&uc=470830241&ic=1514988182&ti=16c8192e22ea32f5d7b5762f6a062c3ac199b79636678234&by=themis");
                //判断当前圆形的是否存在,如果存在,直接消失
                if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
                //调用Presenter的方法
                mPresenter.downloadFile(editInput.getText().toString().trim());
                break;
            default:
                break;
        }
    }

    private void checkNeedPermission() {
        if (getApplicationInfo().targetSdkVersion >= 23) {
            String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //此时没有权限
                ActivityCompat.requestPermissions(this, permissions, 1);
            } else {
                Toast.makeText(this, "权限获取成功,请点击下载", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限获取成功,请点击下载", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "权限获取失败", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    @Override
    public void onStartLoading() {
        ProgressBar progressBar = null;
        if (TYPE == 0) {
            progressBar = this.progressBar;
        } else if (TYPE == 1) {
            progressBar = this.progressBarHor;
        }
        if (progressBar != null && progressBar.getVisibility() == View.GONE) {
            final ProgressBar finalProgressBar = progressBar;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finalProgressBar.setVisibility(View.VISIBLE);
                }
            });
        }
    }
    @Override
    public void onLoading(final int progress) {
        ProgressBar progressBar = null;
        if (TYPE == 0) {
            progressBar = this.progressBar;
        } else if (TYPE == 1) {
            progressBar = this.progressBarHor;
        }
        if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
            final ProgressBar finalProgressBar = progressBar;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finalProgressBar.setProgress(progress);
                }
            });
        }
    }
    @Override
    public void onStopLoading(final Object data) {
        ProgressBar progressBar = null;
        if (TYPE == 0) {
            progressBar = this.progressBar;
        } else if (TYPE == 1) {
            progressBar = this.progressBarHor;
        }
        Log.d(TAG, "onStopLoading: ");

        if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
            final ProgressBar finalProgressBar = progressBar;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finalProgressBar.setVisibility(View.GONE);
                }
            });
        }
        if (data != null) {
            tvLoadResult.post(new Runnable() {
                @Override
                public void run() {
                    tvLoadResult.setText(data.toString());
                }
            });
        }
    }
    @Override
    public void onLoadingError(final Exception e) {
        Log.d(TAG, "onLoadingError: ");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "异常:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        onStopLoading(e.getMessage().toString());
    }
}

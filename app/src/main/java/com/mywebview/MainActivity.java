package com.mywebview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    WebView mWebView;
    Button button;
    final int vision= Build.VERSION.SDK_INT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        mWebView = (WebView) findViewById(R.id.webView);

        WebSettings webSettings = mWebView.getSettings();

        //设置与js交互的权限
        webSettings.setJavaScriptEnabled(true);
        //设置允许js弹出框
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        //先载入js代码
        mWebView.loadUrl("file:///android_asset/javascript.html");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        // 注意调用的JS方法名要对应上
                        // 调用javascript的callJS()方法

                        if (vision<18){
                            mWebView.loadUrl("javascript:callJS()");
                        }else {
                            mWebView.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String s) {

                                }
                            });
                        }

                    }
                });
            }
        });


        // 由于设置了弹窗检验调用结果,所以需要支持js对话框
        // webview只是载体，内容的渲染需要使用webviewChromClient类去实现
        // 通过设置WebChromeClient对象处理JavaScript的对话框
        //设置响应js 的Alert()函数
        mWebView.setWebChromeClient(new WebChromeClient(){

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }
        });
    }
}

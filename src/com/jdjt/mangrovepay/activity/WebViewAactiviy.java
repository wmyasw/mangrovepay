package com.jdjt.mangrovepay.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.util.Handler_String;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.common.BaseFragmentActivity;

@InjectLayer(value = R.layout.acitivity_webview, parent = R.id.center_common, isTitle = true)
public class WebViewAactiviy extends BaseFragmentActivity {
	@InjectView(R.id.wv_content)
	private WebView wv_content;
	private Handler mHandler = new Handler();

	@InjectInit
	public void init() {
		
		String url=getIntent().getStringExtra("url");
		String title=getIntent().getStringExtra("title");
		Ioc.getIoc().getLogger().i(url);
		if(Handler_String.isBlank(title)){
			textview_title.setText("度假宝服务协议");
		}else{
			textview_title.setText(title);
		}
		redictWeb(url);
	}

	@SuppressLint("JavascriptInterface")
	private void redictWeb(String url) {
		WebSettings webSettings = wv_content.getSettings();
		webSettings.setJavaScriptEnabled(true);
		wv_content.addJavascriptInterface(new Object() {
			public void clickOnAndroid() {
				mHandler.post(new Runnable() {
					public void run() {
						wv_content.loadUrl("javascript:wave()");
					}
				});
			}
		}, "");
		wv_content.loadUrl(url);
	}
}

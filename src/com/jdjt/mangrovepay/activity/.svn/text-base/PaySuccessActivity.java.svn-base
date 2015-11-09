package com.jdjt.mangrovepay.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.inject.InjectListener;
import com.android.pc.ioc.inject.InjectMethod;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.view.listener.OnClick;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.common.BaseFragmentActivity;

/*************************************************
 * @copyright:bupt
 * @author: 王明雨
 * @date:2015-1-13
 * @Description:TODO (支付成功页)
 **************************************************/
@InjectLayer(value = R.layout.acitivity_pay_success, parent = R.id.center_common, isTitle = true)
public class PaySuccessActivity extends BaseFragmentActivity {

	@InjectView(R.id.tx_merchant_name)
	TextView tx_merchant_name;
	@InjectView(R.id.tx_price)
	TextView tx_price;
	@InjectView(R.id.btn_paysuccess_next)
	Button btn_paysuccess_next;
	@InjectInit
	private void  init(){
		img_back.setVisibility(View.GONE);
		Intent intent=getIntent();
		String merchantName=intent.getStringExtra("merchantName");
		String price=intent.getStringExtra("price");
		String title=intent.getStringExtra("title");
		tx_merchant_name.setText(merchantName);
		textview_title.setText(title);
		tx_price.setText(getString(R.string._amount,price));
	}
	@InjectMethod(@InjectListener(ids={R.id.btn_paysuccess_next},listeners={OnClick.class}))
	public void click(View v){
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
		startActivity(intent);
		finish();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复
//			// do something here
			Intent intent = new Intent();
			intent.setClass(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}

package com.jdjt.mangrovepay.common;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectListener;
import com.android.pc.ioc.inject.InjectMethod;
import com.android.pc.ioc.inject.InjectPLayer;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.internet.InternetConfig;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.util.Handler_Network;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.application.MangrovePayApp;
import com.jdjt.mangrovepay.contanst.ErrorMsgEnum;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.DialogUtil;

@InjectPLayer(R.layout.activity_common)
public class BaseFragmentActivity extends FragmentActivity {

	@InjectView(R.id.textview_title)
	public TextView textview_title;
	@InjectView(R.id.img_back)
	public ImageButton img_back;
	@InjectView(R.id.btn_leftmenu)
	public ImageView btn_leftmenu;
	@InjectView(R.id.btn_manage)
	public Button btn_manage;
	@InjectView(R.id.layout_title)
	public LinearLayout layout_title;
	// 模式化窗口。用作遮罩层
	protected Dialog dialog;
	protected InternetConfig config;

	protected void showDialog() {
		if (dialog == null) {
			dialog = DialogUtil.createLoadingDialog(this, "请稍候...");
		}
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	protected void cancelDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.cancel();
		}
	}

	/**
	 * 隐藏
	 * 
	 * @author wangmingyu
	 * @2014-9-23@下午1:25:27 void
	 */
	protected void hideDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.hide();
		}
	}

	/*************************************************
	 * @Title: showDialog
	 * @Description: TODO(遮罩层展示)
	 * @param msg
	 *            提示信息
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-14
	 *************************************************/
	protected void showDialog(String msg) {
		if (dialog == null) {
			dialog = DialogUtil.createLoadingDialog(this, msg);
		}
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	/**
	 * 销毁
	 * 
	 * @author wangmingyu
	 * @2014-9-23@下午1:25:20 void
	 */
	protected void dismissDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	@InjectInit
	public void CommonInit() {
		config = new InternetConfig();
		config.setHead(CommonUtils.inHeaders());
//		IntentFilter filter = new IntentFilter();
//		filter.addAction("finish");
//		registerReceiver(mFinishReceiver, filter);
		MangrovePayApp.getInstance().addActivity(this); 
		// 验证手机是否联网
		if (!Handler_Network.isNetworkAvailable(this)) {
			Toast.makeText(this, ErrorMsgEnum.NET_ERROR.getName(),
					Toast.LENGTH_SHORT).show();
			return;
		}

	}

	protected BroadcastReceiver mFinishReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			context.unregisterReceiver(this);
			if ("finish".equals(intent.getAction())) {

				Ioc.getIoc().getLogger().i("I am " + getLocalClassName()
				+ ",now finishing myself...");
				finish();
			}
		}

	};

	@InjectMethod(@InjectListener(ids = { R.id.img_back }, listeners = { OnClick.class }))
	public void onBackClick(View v) {
		finish();
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dismissDialog();
		Ioc.getIoc().getLogger().i("触发 onDestroy");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Ioc.getIoc().getLogger().i("触发 onPause");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Ioc.getIoc().getLogger().i("触发 onResume");
	}

	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
		Ioc.getIoc().getLogger().i("触发 onPostResume");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Ioc.getIoc().getLogger().i("触发 onStop");
	}

}
package com.jdjt.mangrovepay.utils;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.pc.util.Handler_String;
import com.jdjt.mangrovepay.R;

/*************************************************
 * @copyright:bupt
 * @author: 王明雨
 * @date:2015-1-9
 * @Description:TODO (重写公共提示框，样式模版可自定义)
 **************************************************/
public class CommonAlert extends PopupWindow {
	Context context;
	android.app.AlertDialog ad;
	TextView titleView;
	TextView messageView;
	LinearLayout buttonLayout;
	Window window;
	boolean cancelable=true;
	
	public void setCancelable(boolean cancelable) {
		ad.setCancelable(cancelable);// 不可以用“返回键”取消
	}
	public CommonAlert(Context context) {
		this.context = context;
		ad = new android.app.AlertDialog.Builder(context).create();
		ad.show();
		// Replace the source alert dialog.
		 window = ad.getWindow();
		window.setContentView(R.layout.activity_alert);
		// titleView = (TextView) window.findViewById(R.id.title);
		messageView = (TextView) window.findViewById(R.id.t_msg);
		buttonLayout = (LinearLayout) window.findViewById(R.id.buttonLayout);
	
	}
	public CommonAlert(Context context,int layout_id) {
		this.context = context;
		ad = new android.app.AlertDialog.Builder(context).create();
		ad.show();
		// Replace the source alert dialog.
		 window = ad.getWindow();
		window.setContentView(layout_id);
		// titleView = (TextView) window.findViewById(R.id.title);
		messageView = (TextView) window.findViewById(R.id.t_msg);
		buttonLayout = (LinearLayout) window.findViewById(R.id.buttonLayout);
	}
	public void setTitle(int resId) {
		titleView.setText(resId);
	}

	public void setTitle(String title) {
		titleView.setText(title);
	}

	public void setMessage(int resId) {
		messageView.setText(resId);
	}

	public void setMessage(String message) {
		messageView.setText(message);
	}

	/**
	 * Button style
	 * 
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(String text,
			final View.OnClickListener listener) {
		Button btn=(Button) window.findViewById(R.id.hotelcard_cancle_banding_btn);
		btn.setOnClickListener(listener);
		if(!Handler_String.isBlank(text))
			btn.setText(text);
//		Button button = new Button(context);
//		LinearLayout.LayoutParams params = new LayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		button.setLayoutParams(params);
//		button.setBackgroundResource(R.drawable.app_listbk);
//		button.setText(text);
//		button.setTextColor(Color.WHITE);
//		button.setTextSize(20);
//		button.setOnClickListener(listener);
//		buttonLayout.addView(button);
	}

	/**
	 * Button style
	 * 
	 * @param text
	 * @param listener
	 */
	public void setNegativeButton(String text,
			final View.OnClickListener listener) {
		Button btn=(Button) window.findViewById(R.id.hotelcard_submit_banding_btn);
		btn.setOnClickListener(listener);
		if(!Handler_String.isBlank(text))
			btn.setText(text);
//		Button button = new Button(context);
//		LinearLayout.LayoutParams params = new LayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		button.setLayoutParams(params);
//		button.setBackgroundResource(R.drawable.app_listbk);
//		button.setText(text);
//		button.setTextColor(Color.WHITE);
//		button.setTextSize(20);
//		button.setOnClickListener(listener);
//		if (buttonLayout.getChildCount() > 0) {
//			params.setMargins(20, 0, 0, 0);
//			button.setLayoutParams(params);
//			buttonLayout.addView(button, 1);
//		} else {
//			button.setLayoutParams(params);
//			buttonLayout.addView(button);
//		}

	}

	/**
	 * dismiss dialog
	 */
	public void dismiss() {
		ad.dismiss();
	}
	public void cancel() {
		ad.cancel();
	}
}

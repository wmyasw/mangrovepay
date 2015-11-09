package com.jdjt.mangrovepay.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.pc.util.Handler_Inject;
import com.jdjt.mangrovepay.R;
/*import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;*/

public class DialogUtil {

	/**
	 * 得到自定义的progressDialog
	 * @param context
	 * @param msg
	 * @return
	 */
	public static  Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.activity_loading, null);// 得到加载view
		
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView imageView= (ImageView) v.findViewById(R.id.imageView1);
		// 从xml中得到GifView的句柄
		TextView tipTextView =(TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		
		// 使用ImageView显示动画
		imageView.startAnimation(hyperspaceJumpAnimation);
		hyperspaceJumpAnimation.reset();
		tipTextView.setText(msg);// 设置加载信息
		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));// 设置布局
		return loadingDialog;

	}
	public static Dialog createDialog(Context context, int layoutid,int viewid) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(layoutid, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(viewid);// 加载布局
		// fragment注解必须走这里
		Handler_Inject.injectFragment(context, v);
		// main.xml中的ImageView
	/*	ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
*/		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
	/*	// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息
*/
		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(true);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return loadingDialog;

	}
}

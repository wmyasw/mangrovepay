package com.jdjt.mangrovepay.common;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.util.Handler_Network;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.contanst.ErrorMsgEnum;
import com.jdjt.mangrovepay.event.SlidingEntity;
import com.jdjt.mangrovepay.utils.DialogUtil;
import com.jdjt.mangrovepay.zxing.view.ViewfinderView;

public class BaseFragment extends Fragment {

	public LayoutInflater inflater;
	public Activity activity;
	public View progress;
	public LinearLayout progress_container;
	private ViewfinderView viewfinderView;
	// 模式化窗口。用作遮罩层
	public Dialog dialogFragment;
	public void showDialog() {
		if (dialogFragment == null) {
			dialogFragment = DialogUtil.createLoadingDialog(activity, "请稍候...");
		}
		if (!dialogFragment.isShowing()) {
			dialogFragment.show();
		}
	}
	/*************************************************
	 @Title: showDialog 
	 @Description: TODO(遮罩层展示) 
	 @param msg    提示信息
	 @return void    返回类型 
	 @throws 
	 @date  2015-1-14
	*************************************************/
	public void showDialog(String msg) {
		if (dialogFragment == null) {
			dialogFragment = DialogUtil.createLoadingDialog(activity,msg);
		}
		if (!dialogFragment.isShowing()) {
			dialogFragment.show();
		}
	}
	/*************************************************
	 @Title: cancelDialog 
	 @Description: TODO(清除遮罩层)     设定文件 
	 @return void    返回类型 
	 @throws 
	 @date  2015-1-14
	*************************************************/
	public void cancelDialog() {
		if (dialogFragment != null && dialogFragment.isShowing()) {
			dialogFragment.cancel();
		}
	}

	/**
	 * 隐藏
	 * 
	 * @author wangmingyu
	 * @2014-9-23@下午1:25:27 void
	 */
	public void hideDialog() {
		if (dialogFragment != null && dialogFragment.isShowing()) {
			dialogFragment.hide();
		}
	}

	/**
	 * 销毁
	 * 
	 * @author wangmingyu
	 * @2014-9-23@下午1:25:20 void
	 */
	public void dismissDialog() {
		if (dialogFragment != null) {
			dialogFragment.dismiss();
			dialogFragment=null;
		}
	}
	public View.OnTouchListener on = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			EventBus eventBus = EventBus.getDefault();
			SlidingEntity slidingEntity = new SlidingEntity();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				slidingEntity.setSlidingEnable(false);
				eventBus.post(slidingEntity);
				break;
			case MotionEvent.ACTION_UP:
				slidingEntity.setSlidingEnable(true);
				slidingEntity.setViewPage(true);
				eventBus.post(slidingEntity);
				break;
			}
			return false;
		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	public void setProgress(View view) {
		if (progress != null) {
			return;
		}
		LayoutParams lp = view.getLayoutParams();
		ViewParent parent = view.getParent();
		FrameLayout container = new FrameLayout(activity);
		ViewGroup group = (ViewGroup) parent;
		int index = group.indexOfChild(view);
		group.removeView(view);
		group.addView(container, index, lp);
		container.addView(view);
		if (inflater != null) {
			progress = inflater.inflate(R.layout.fragment_progress, null);
			progress_container = (LinearLayout) progress.findViewById(R.id.progress_container);
			container.addView(progress);
			progress_container.setTag(view);
			view.setVisibility(View.GONE);
		}
		group.invalidate();
	}
	public void startProgress() {
		if (progress_container != null) {
			progress_container.setVisibility(View.VISIBLE);
		}
		hideProgress();
	}
	public void endProgress() {
		if (progress_container != null) {
			((View) progress_container.getTag()).setVisibility(View.VISIBLE);
			progress_container.setVisibility(View.GONE);
		}
	}

	Handler handler;

	public void hideProgress() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				endProgress();
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);
			}
		}).start();
	}
	
	@InjectInit
	public void CommonInit() {
		viewfinderView = (ViewfinderView) activity.findViewById(R.id.viewfinder_view);
		// 验证手机是否联网
		if (!Handler_Network.isNetworkAvailable(activity)) {
			Toast.makeText(activity, ErrorMsgEnum.NET_ERROR.getName(),
					Toast.LENGTH_SHORT).show();
			return;
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dismissDialog();
	}
	
}
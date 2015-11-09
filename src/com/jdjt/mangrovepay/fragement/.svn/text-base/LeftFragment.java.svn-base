package com.jdjt.mangrovepay.fragement;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectHttpOk;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectListener;
import com.android.pc.ioc.inject.InjectMethod;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.internet.FastHttpHander;
import com.android.pc.ioc.internet.InternetConfig;
import com.android.pc.ioc.internet.ResponseEntity;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.util.Handler_Inject;
import com.android.pc.util.Handler_Json;
import com.google.gson.JsonObject;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.activity.CaptureActivity;
import com.jdjt.mangrovepay.application.MangrovePayApp;
import com.jdjt.mangrovepay.common.BaseFragment;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.event.FragmentEntity;
import com.jdjt.mangrovepay.event.MSGCountEntity;
import com.jdjt.mangrovepay.event.MainEntity;
import com.jdjt.mangrovepay.event.SlidingToggleEntity;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.ResultParse;
import com.jdjt.mangrovepay.view.BadgeView;

/*
 * Author: mars
 * Created Date:2014-1-21
 * Copyright @ 2014 BU
 * Description: 类描述
 *
 * History:
 */
public class LeftFragment extends BaseFragment {

	EventBus eventBus = EventBus.getDefault();
	FragmentEntity fe;
	@InjectView(R.id.left_title)
	public  TextView left_title;
	InputMethodManager imm;
	@InjectView(R.id.radioGroup1)
	public RadioGroup radioGroup1;
	@InjectView(R.id.ll_msg_info)
	public Button ll_msg_info;
	public BadgeView badge;
	CommonUtils cu;
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View rootView = inflater.inflate(R.layout.activity_left, container,
				false);
		this.view = rootView;
		Handler_Inject.injectFragment(this, rootView);
		return rootView;
	}

	@InjectInit
	private void init() {
		left_title.setText(MangrovePayApp.app.getData("account"));
		eventBus.register(this,"onEventShowCount");
		checkAccount(MangrovePayApp.app.getData("account"));
		//
		// eventBus.register(this, "onGet");
		// cu=CommonUtils.newInstence();
		// cu.careteMsg(activity, ll_msg_info);
	}
	public void checkAccount(String account) {
//		showDialog();
		String url = Url.MEM_INFO_ARRAY;
		JsonObject json = new JsonObject();
		json.addProperty("account", account);
		json.addProperty("subType", "");
		json.addProperty("includeSubAcc", "1");
		InternetConfig config = new InternetConfig();
		config.setKey(2);
		config.setHead(CommonUtils.inHeaders());
		FastHttpHander.ajaxString(url, json.toString(), config, this);
	}
	@InjectHttpOk(value={2})
	public void result(ResponseEntity r){
		if(ResultParse.isResultOK(r, activity)){
			HashMap<String,String> map=Handler_Json.JsonToCollection(r.getContentAsString());
			String cardNo=map.get("virtualAccountNo");
			MangrovePayApp.app.setData("virtualAccountNo", cardNo);
		}
	}
	/*************************************************
	 * @Title: onMenuClick
	 * @Description: TODO(左侧菜单选择)
	 * @param v
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-15 R.id.ll_account_recharge, R.id.ll_msg_info,
	 *       R.id.ll_setting, R.id.ll_sm, R.id.ll_transfer, R.id.ll_wallet
	 *************************************************/
	@InjectMethod(value = @InjectListener(ids = { R.id.ll_account_recharge,
			R.id.ll_msg_info, R.id.ll_setting, R.id.ll_sm, R.id.ll_transfer,
			R.id.ll_wallet }, listeners = { OnClick.class }))
	public void onMenuClick(View v) {
		Fragment fragment = null;
		boolean isShow = true;
		String title = "";
		switch (v.getId()) {
		case R.id.ll_account_recharge:
			fragment = new RechargeFragment();
			title = "账户充值";
			break;
		case R.id.ll_msg_info:
			fragment = new MSGInfoFragment();
			if(null!=badge)
				badge.setVisibility(View.GONE);
			title = "消息中心";
			break;
		case R.id.ll_setting:
			fragment = new SettingFragment();
			title = "设置";
			break;
		case R.id.ll_sm:
			/*
			 * fragment=new SMFragment(); title="扫码支付";
			 */
			startActivity(new Intent()
					.setClass(activity, CaptureActivity.class));
			activity.finish();
			return;

		case R.id.ll_transfer:
			fragment = new TransferFragment();
			title = "转账";
			break;
		case R.id.ll_wallet:
			fragment = new HomeFragment();
			title = "度假宝钱包";
			isShow = false;
			break;
		default:
			break;
		}
		FragmentEntity fe = new FragmentEntity();
		fe.setFragment(fragment);
		eventBus.post(fe);// 替换主页内容
		SlidingToggleEntity s = new SlidingToggleEntity();
		s.setSlidingEnable(true);// 选择完成后关闭 左侧菜单
		eventBus.post(s);
		MainEntity me = new MainEntity();
		me.setViewEnable(isShow);
		me.setTitle_name(title);
		eventBus.post(me);// 隐藏首页的 tab
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		eventBus.unregister(this);
	}
	public void onEventShowCount(MSGCountEntity ce){
		if(ce.isRead()){
			badge = new BadgeView(activity, ll_msg_info);// 创建一个BadgeView对象，view为你需要显示提醒信息的控件
			badge.setWidthAndHight(12);
			badge.setBadgePosition(BadgeView.POSITION_TOP_LEFT);// 显示的位置.中间，还有其他位置属性
			// badge.setTextColor(Color.WHITE); //文本颜色
			badge.setBadgeBackgroundColor(Color.RED); // 背景颜色
			badge.setBadgeMargin(40, 24); // 水平和竖直方向的间距
			// badge.setBadgeMargin(0);//各边间距
			badge.toggle(); // 设置，还要加这句,已经显示则影藏，否则显示
		}
	}
}

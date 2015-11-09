package com.jdjt.mangrovepay.wxapi;

import java.util.HashMap;
import java.util.Random;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.util.DialogUtil;
import com.android.pc.util.Handler_Json;
import com.jdjt.mangrovepay.contanst.Constants;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class PayActivity1 {
	// 模式化窗口。用作遮罩层
	protected Dialog dialog;
	private IWXAPI api;
	Context context;

	public PayActivity1(Context context, HashMap<String, String> map) {
		api = WXAPIFactory.createWXAPI(context, Constants.APP_ID);
		api.registerApp(Constants.APP_ID);
		this.context = context;
		boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		Ioc.getIoc().getLogger().i("当前是否可以发送微信" + isPaySupported);
		if (isPaySupported) {
			sendPayReq(map);
		} else {
			Toast.makeText(context, "请检查您的是否有微信或版本是否支持微信支付", Toast.LENGTH_SHORT)
					.show();
		}
	}

	protected void showDialog() {
		if (dialog == null) {
			dialog = DialogUtil.createLoadingDialog(context, "正在加载...");
		}
		if (!dialog.isShowing()) {
			dialog.show();
		}

	}

	protected void cancelDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	/**
	 * body：商品名称 outTradeNo：订单号（随便输入都可以） spbillCreateIP：手机端IP地址 totalMoney：金额
	 * 
	 * @author wangmingyu
	 * @2014-9-13@下午4:37:26 void
	 */
	/**
	 * 注意：商户系统内部的订单号,32个字符内、可包含字母,确保在商户系统唯一
	 */
	private String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	/**
	 * 
	 * 调用开放平台接口发送支付请求 将第二步生成的 prepayId 作为参数，调用微信 sdk 发送支付请求到微信
	 * 
	 * @param result
	 * @author wangmingyu
	 * @2014-9-13@上午11:11:00 void
	 */
	private void sendPayReq(HashMap<String, String> map) {
		Ioc.getIoc().getLogger().i("调用微信支付接口");
		try {
			PayReq req = new PayReq();
			req.transaction=map.get("payOrderNo");
			req.appId			= Constants.APP_ID;
			req.partnerId		= Constants.PARTNER_ID;
			//req.partnerId = map.get("partnerId");
			req.prepayId = map.get("prepayId");
			req.nonceStr = map.get("nonceStr");
			req.timeStamp =map.get("timeStamp");
			req.packageValue = "Sign=WXPay";//+map.get("packageValue");
			req.sign			= map.get("sign");
			req.extData			= map.get("payOrderNo")+"_"+map.get("payAmount"); // optional
			Ioc.getIoc().getLogger()
					.i("调用微信支付接口 " + Handler_Json.beanToJson(req));
			// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
			api.sendReq(req);
		} catch (Exception e) {
			Ioc.getIoc().getLogger().i("调用微信支付异常"+e.getMessage());
		}

	}
}

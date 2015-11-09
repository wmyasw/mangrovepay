package com.jdjt.mangrovepay.wxapi;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.util.MapVo;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_String;
import com.google.gson.JsonObject;
import com.jdjt.mangrovepay.activity.PaySuccessActivity;
import com.jdjt.mangrovepay.contanst.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;
	String amount;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_com);

		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		Ioc.getIoc().getLogger().i("onReq微信结果返回页面：" + Handler_Json.beanToJson(req));
		String[] m=req.transaction.split("_");
		amount=m[1];
	}

	@Override
	public void onResp(BaseResp resp) {
		
		Ioc.getIoc().getLogger().i("onResp微信结果返回页面：" +Handler_Json.beanToJson(resp));
		Ioc.getIoc().getLogger().i("微信结果返回页面：errStr" + resp.errStr);
		Ioc.getIoc().getLogger().i("微信结果返回页面：getType" + resp.getType());
		Ioc.getIoc().getLogger().i("微信结果返回页面：openId" + resp.openId);
		Ioc.getIoc().getLogger().i("微信结果返回页面： transaction" + resp.transaction);
		Ioc.getIoc().getLogger().i("微信结果返回页面：errCode" + resp.errCode);
		HashMap<String, String> map=Handler_Json.JsonToCollection(Handler_Json.beanToJson(resp));
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode == 0 || "0".equals(resp.errCode + "")) {
				// Intent i=getIntent();
				
				String orderId = (String) MapVo.get("orderId");
				Intent intent = new Intent(this,
						PaySuccessActivity.class);
				String[] m=map.get("extData").split("_");
				intent.putExtra("price",m[1]);
				intent.putExtra("title", "微信支付");
				intent.putExtra("merchantName", m[0]);
				
				startActivity(intent);
			}
			finish();
		}
	}
}
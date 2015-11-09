package com.jdjt.mangrovepay.fragement;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectHttpErr;
import com.android.pc.ioc.inject.InjectHttpOk;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectListener;
import com.android.pc.ioc.inject.InjectMethod;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.internet.FastHttpHander;
import com.android.pc.ioc.internet.InternetConfig;
import com.android.pc.ioc.internet.ResponseEntity;
import com.android.pc.ioc.verification.Rule;
import com.android.pc.ioc.verification.Validator;
import com.android.pc.ioc.verification.Validator.ValidationListener;
import com.android.pc.ioc.verification.annotation.NumberRule;
import com.android.pc.ioc.verification.annotation.NumberRule.NumberType;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.util.Handler_Inject;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_String;
import com.google.gson.JsonObject;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.adpater.HotelBalanceAdapter;
import com.jdjt.mangrovepay.common.BaseFragment;
import com.jdjt.mangrovepay.contanst.ErrorMsgEnum;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.event.OlinePayDto;
import com.jdjt.mangrovepay.event.WXPayDto;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.ResultParse;
import com.jdjt.mangrovepay.wxapi.PayActivity1;

/*************************************************
 * @copyright:bupt
 * @author: 王明雨
 * @date:2015-1-9
 * @Description:TODO (充值)
 **************************************************/
public class RechargeFragment extends BaseFragment implements ValidationListener{
	EventBus eventBus = EventBus.getDefault();

	ArrayList<HashMap<String, String>> mapList = null;
	HotelBalanceAdapter adapter = null;
	@NumberRule(message="充值金额最小为0.01", order = 1, type = NumberType.FLOAT,gt=0.00)
	@InjectView(R.id.recharge_price)
	public EditText recharge_price;
	@InjectView(R.id.btn_recharge)
	public Button btn_recharge;
	Validator validator;
	String orderNo;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View rootView = inflater.inflate(R.layout.fragement_recharge, container,
				false);
		Handler_Inject.injectFragment(this, rootView);
		return rootView;
	}

	@InjectInit
	public void init() {
		// 验证
		
		// getAccountInfo();
		//获取输入的金额。并强制限制2位小数~
		recharge_price.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				 if (s.toString().contains(".")) {
	                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
	                        s = s.toString().subSequence(0,
	                                s.toString().indexOf(".") + 3);
	                        recharge_price.setText(s);
	                        recharge_price.setSelection(s.length());
	                    }
	                }
	                if (s.toString().trim().substring(0).equals(".")) {
	                    s = "0" + s;
	                    recharge_price.setText(s);
	                    recharge_price.setSelection(2);
	                }
	 
	                if (s.toString().startsWith("0")
	                        && s.toString().trim().length() > 1) {
	                    if (!s.toString().substring(1, 2).equals(".")) {
	                    	recharge_price.setText(s.subSequence(0, 1));
	                    	recharge_price.setSelection(1);
	                        return;
	                    }
	                }
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(!Handler_String.isBlank(s.toString())){
					btn_recharge.setEnabled(true);
				}else{
					btn_recharge.setEnabled(false);
				}
			}
		});
	}
	@InjectMethod(@InjectListener(ids={R.id.btn_recharge},listeners={OnClick.class}))
	public  void onBtnClick(View v){
		validator = new Validator(this);
		validator.setValidationListener(this);
		validator.validate();
	}

	@Override
	public void onValidationFailed(View arg0, Rule<?> arg1) {
		// TODO Auto-generated method stub
		String message = arg1.getFailureMessage();
		CommonUtils.onAlertToast(arg0, message, activity);
	}

	@Override
	public void onValidationSucceeded() {
		// TODO Auto-generated method stub
		getOrderNo();
//		 Toast.makeText(activity, "验证成功", Toast.LENGTH_SHORT).show();
	}

	 @Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		recharge_price.clearFocus();
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(recharge_price.getWindowToken(), 0);
		Ioc.getIoc().getLogger().i("fragement 被覆盖");
	}

	private void getOrderNo(){
		showDialog();
		String url=Url.METHOD_REQ_RECHARGE;
//		 String url="http://192.168.10.2:8080/uum/mem/account/recharge/req_recharge.json";
		 JsonObject json=new JsonObject();
		 json.addProperty("money", recharge_price.getText().toString().trim());
		 InternetConfig config=new InternetConfig();
		 config.setKey(1);
		 config.setHead(CommonUtils.inHeaders());
		 FastHttpHander.ajaxString(url, json.toString(), config, this);
	 }
	 @InjectHttpOk(1)
	 public void resultOK(ResponseEntity r){
		 dismissDialog();
		if(ResultParse.isResultOK(r, activity)){
			//String orderNo=(String) Handler_Json.getValue("orderNo", r.getContentAsString());
			String payOrderNo= (String) Handler_Json.getValue("payOrderNo", r.getContentAsString());
			getWxProduct(payOrderNo);
		}
	 }
	 @InjectHttpErr
	 public void resultErr(ResponseEntity r){
		 Toast.makeText(activity, ErrorMsgEnum.NET_ERROR.getName(), Toast.LENGTH_SHORT).show();
	 }
	/**
	 * 微支付
	 * 
	 * @author wangmingyu
	 * @2014-9-15@下午8:47:21 void
	 */
	private void getWxProduct(String orderNo) {
		showDialog();
		String url = Url.PAYMENT_AMOUNT;
//		String url="http://192.168.10.2:8080/pay/payment/wechat/prepay.json";
		WXPayDto wp = new WXPayDto();
		OlinePayDto opt = new OlinePayDto();
		// 获取wifi服务
		android.net.wifi.WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
		String ip = "";
		// 判断wifi是否开启
		if (!wifiManager.isWifiEnabled()) {
			// wifiManager.setWifiEnabled(true);
			ip = CommonUtils.getPsdnIp();
		} else {
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			ip = CommonUtils.intToIp(ipAddress);
		}

//		opt.setClientIp(ip);
//		opt.setPayDiv("1");
//		opt.setAmount(recharge_price.getText().toString().trim());
//		wp.setOnlineInfo(opt);
//		wp.setOrderIdList(orderNo);
//		String jsonParams = Handler_Json.beanToJson(wp);
//		System.out.println(jsonParams);
//		支付订单号	payOrderNo	String	N	
//		支付类型	payType	String	N	1消费;2:充值4捐赠;
//		支付金额	payAmount	Double	N	用于验证金额。
//		客户端IP	clientIp	String	N	

		JsonObject jsonParams=new JsonObject();
		this.orderNo=orderNo;
		jsonParams.addProperty("payOrderNo", orderNo+"");
		jsonParams.addProperty("payType", 2+"");
		jsonParams.addProperty("payAmount", recharge_price.getText().toString().trim());
		jsonParams.addProperty("clientIp", ip);
		InternetConfig config = new InternetConfig();
		config.setHead(CommonUtils.inHeaders());
		config.setKey(101);
		FastHttpHander.ajaxString(url, jsonParams.toString(), config, this);
	}
	
	/*************************************************
	 @Title: wxResult 
	 @Description: TODO(微信支付返回) 
	 @param r    设定文件 
	 @return void    返回类型 
	 @throws 
	 @date  2014-11-19
	*************************************************/
	@InjectHttpOk(101)
	public void wxResult(ResponseEntity r) {
	
		Ioc.getIoc().getLogger().i("正在调取微信端服务。。。。");
		if(ResultParse.isResultOK(r, activity)){
			HashMap<String, String> map = (HashMap<String, String>) Handler_Json.JsonToCollection(r.getContentAsString());
			map.put("payOrderNo", orderNo+"");
			map.put("payAmount", recharge_price.getText().toString().trim());
			new PayActivity1(activity, map);
			dismissDialog();
		}else{
			Ioc.getIoc().getLogger().i("正在调取微信端服务 异常。。。。");
			dismissDialog();
		}
	}
	
}

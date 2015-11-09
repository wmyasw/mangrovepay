package com.jdjt.mangrovepay.activity;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.inject.InjectHttpErr;
import com.android.pc.ioc.inject.InjectHttpOk;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.inject.InjectListener;
import com.android.pc.ioc.inject.InjectMethod;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.internet.FastHttpHander;
import com.android.pc.ioc.internet.InternetConfig;
import com.android.pc.ioc.internet.ResponseEntity;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_String;
import com.google.gson.JsonObject;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.common.BaseFragmentActivity;
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
 * @date:2015-1-13
 * @Description:TODO (在线支付)
 **************************************************/
@InjectLayer(value = R.layout.acitivity_pay, parent = R.id.center_common, isTitle = true)
public class QROnlinePayActivity extends BaseFragmentActivity {

	@InjectView(R.id.tx_order_no)
	TextView tx_order_no; // 订单号
	@InjectView(R.id.tx_discount)
	TextView tx_discount;// 折扣
	@InjectView(R.id.tx_merchant)
	TextView tx_merchant;// 商户
	@InjectView(R.id.tx_amount_payable)
	TextView tx_amount_payable;// 应付金额
	@InjectView(R.id.tx_price)
	TextView tx_price;// 实付金额
	@InjectView(R.id.pay_channel_name)
	TextView pay_channel_name;// 支付渠道
	private String orderno;
	private String cardNo;
	private String amount;//支付金额
	private String orderAmount;//订单金额
	private AlertDialog dlg;
	private String passwrod;// 支付密码
	
	int type;

	private final String PAY_SUCCESS="1";//支付成功
	private final String PAY_CHULI="0";//处理中
	private final String PAY_ERR="2";//支付失败
	@InjectInit
	public void init() {
		textview_title.setText("支付");
		ajaxPost();
	}

	public void ajaxPost() {
		showDialog("正在加载...");
		orderno = getIntent().getStringExtra("order_no");
		String url = Url.PAYMENT_ORDER_INFO_URL;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("payOrderNo", orderno);
		InternetConfig config = new InternetConfig();
		config.setKey(1);
		FastHttpHander.ajaxGet(url, map, config, this);
	}

	/*************************************************
	 * @Title: nextClick
	 * @Description: TODO(下一步操作，打开支付密码框)
	 * @param v
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-19
	 *************************************************/
	@InjectMethod(@InjectListener(ids = { R.id.btn_pay_next,
			R.id.rl_pay_channel }, listeners = { OnClick.class }))
	public void nextClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_pay_next: // 下一步支付
			if (null != pay_channel_name.getTag()) {
				if ("Wechat".equals(pay_channel_name.getTag())) {
					// 此处调用微信支付
//					Toast.makeText(this, "微信审核暂未通过", Toast.LENGTH_SHORT).show();
					getWxProduct(orderno);
				} else {
					// 此处默认为 红树林卡或度假宝账户支付方式
					alertPayPwd();
				}
			} else {
				Toast.makeText(this, "选择支付方式", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.rl_pay_channel:
			Intent intent = new Intent().setClass(this,
					PayChannelAcitivity.class);
			intent.putExtra("payOrderNo", tx_order_no.getText() + "");
			Ioc.getIoc().getLogger().i("选择后的渠道code" + pay_channel_name.getTag());
			intent.putExtra("channelCode", pay_channel_name.getTag() + "");
			startActivityForResult(intent, 1);
			break;
		default:
			break;
		}
	}

	/*************************************************
	 @Title: onActivityResult 
	 @Description: TODO(选择渠道返回) 
	 @param arg0
	 @param arg1
	 @param data    设定文件 
	 @throws 
	 @date  2015-1-29
	 @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	*************************************************/
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(arg0, arg1, arg2);
		Ioc.getIoc().getLogger().i("resultCode为回传的标记，我在B中回传的是RESULT_OK"+resultCode);
		switch (resultCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case RESULT_OK:
			Ioc.getIoc().getLogger().i(data);
			Ioc.getIoc().getLogger().i(data.getExtras());
			if(null!=data &&null!=data.getExtras()){
				String channelCode = data.getStringExtra("channelCode");
				String channelName = data.getStringExtra("channelName");
				cardNo = data.getStringExtra("cardNo");
				 type = Integer.valueOf(data.getStringExtra("type"));
				Ioc.getIoc().getLogger().i("channelName：" + channelName);
				Ioc.getIoc().getLogger().i("channelCode：" + channelCode);
				if (!Handler_String.isBlank(channelName)) {
					pay_channel_name.setText(channelName);
					pay_channel_name.setTag(channelCode);
				}
				switch (type) {
				case 1:
					break;
				case 2:
					getAccountCalculate(cardNo);// 账户折扣计算
					break;
				case 3:
					getCardCalculate(cardNo); // 储值卡折扣计算
					break;
				default:
					break;
				}
				break;
			}
		default:
			break;
		}
	}

	/*************************************************
	 * @Title: alertPayPwd
	 * @Description: TODO(自定义密码输入) 设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-19
	 *************************************************/
	private void alertPayPwd() {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.activity_pay_alert, null);
		dlg = new AlertDialog.Builder(this).create(); // 初始化dialog
		dlg.setView(view);// 重写dialog 模版
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.activity_pay_alert);// 再次设置。是为了修正 在dialog
															// 添加edit 不能调用键盘的bug
		/* 获取自定义按钮 及文本 */
		Button cancle_btn = (Button) window.findViewById(R.id.btn_clear);
		final Button ok = (Button) window.findViewById(R.id.btn_ok);
		EditText pay_pwd = (EditText) window.findViewById(R.id.tx_pay_pwd);
		TextView tx_pay_merchant = (TextView) window
				.findViewById(R.id.tx_pay_merchant);
		TextView tx_alert_title = (TextView) window
				.findViewById(R.id.tx_alert_title);
		TextView tx_pay_amount = (TextView) window
				.findViewById(R.id.tx_pay_amount);
		tx_pay_merchant.setText(tx_merchant.getText());
		tx_pay_amount.setText(tx_price.getText());
		tx_alert_title.setText("请输入支付密码");

		// 设置文本框输入监听事件 如果不为空那么 确定按钮变为可用状态
		pay_pwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (!Handler_String.isBlank(s.toString())
						&& s.toString().length() >= 6) {
					passwrod = s.toString();
					ok.setEnabled(true);
				}
			}
		});
		// 支付密码 取消按钮
		cancle_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dlg.cancel();
			}
		});
		// 支付密码 确定按钮
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// unbindAjax(cardNo);
				switch (type) {
				case 2:// 账户支付
					sedAccountPay();
					break;
				case 3:// 储值卡支付
					sedCardPay();
					break;
				default:
					break;
				}
				dlg.cancel();
			}
		});
	}

	/*************************************************
	 * @param cardNo
	 * @Title: getCardCalculate
	 * @Description: TODO(储值卡扣款计算) 设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-21
	 *************************************************/
	private void getCardCalculate(String cardNo) {
		showDialog();
		String url = Url.PAYMENT_CARD_CALCULATE_URL;
		JsonObject json = new JsonObject();
		json.addProperty("payOrderNo", orderno);
		json.addProperty("amount",orderAmount);
		json.addProperty("cardNo", cardNo);
		json.addProperty("subType", pay_channel_name.getTag() + "");
		config = new InternetConfig();
		config.setKey(2);
		config.setHead(CommonUtils.inHeaders());
		FastHttpHander.ajaxString(url, json.toString(), config, this);

	}

	/*************************************************
	 @Title: getAccountCalculate 
	 @Description: TODO(账户余额扣款计算) 
	 @param cardNo    设定文件 
	 @return void    返回类型 
	 @throws 
	 @date  2015-3-24
	*************************************************/
	private void getAccountCalculate(String cardNo) {
		showDialog();
		String url = Url.DJB_ACCOUNT_CALCULATE_URL;
		JsonObject json = new JsonObject();
		json.addProperty("payOrderNo", orderno);
		json.addProperty("amount", orderAmount);
		json.addProperty("virtualAccountNo", cardNo);
		json.addProperty("subType", pay_channel_name.getTag() + "");
		config = new InternetConfig();
		config.setKey(3);
		config.setHead(CommonUtils.inHeaders());
		FastHttpHander.ajaxString(url, json.toString(), config, this);

	}

	/*************************************************
	 * @Title: sedAccountPay
	 * @Description: TODO(账户支付) 设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-21
	 *************************************************/
	private void sedAccountPay() {
		showDialog("正在提交数据...");
		String url = Url.DJB_ACCOUNT_PAY_URL;
		JsonObject json = new JsonObject();
		json.addProperty("payOrderNo", orderno);
		json.addProperty("amount", orderAmount);
		json.addProperty("virtualAccountNo", cardNo);
		json.addProperty("subType", pay_channel_name.getTag()+"");
		json.addProperty("password", passwrod);
		json.addProperty("payAmount", amount);
		//json.addProperty("moreAmount", "0");
		config = new InternetConfig();
		config.setKey(4);
		config.setHead(CommonUtils.inHeaders());
		FastHttpHander.ajaxString(url, json.toString(), config, this);

	}

	/*************************************************
	 * @Title: sedCardPay
	 * @Description: TODO(卡支付) 设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-21
	 *************************************************/
	private void sedCardPay() {
		String url = Url.PAYMENT_EPAYCARD_EXEC_PAY_URL;
		JsonObject json = new JsonObject();
		json.addProperty("payOrderNo", orderno);
		json.addProperty("amount", orderAmount);
		json.addProperty("cardNo", cardNo);
		json.addProperty("subType", pay_channel_name.getTag()+"");
		json.addProperty("password", passwrod);
		json.addProperty("payAmount", amount);
		//json.addProperty("moreAmount", "0");
		config = new InternetConfig();
		config.setKey(5);
		config.setHead(CommonUtils.inHeaders());
		FastHttpHander.ajaxString(url, json.toString(), config, this);

	}

	private void setIntiMain(HashMap<String, String> map) {
		dismissDialog();
		if (null != map) {
			tx_order_no.setText(map.get("payOrderNo"));
			tx_discount.setText(getString(R.string._amount,
					map.get("discountAmount")));
			tx_merchant.setText(map.get("businessUserName"));
			tx_amount_payable.setText(getString(R.string._amount,
					map.get("amount")));
			// tx_price.setText(map.get("paidAmount"));
			tx_price.setText(getString(R.string._amount, map.get("amount")));
			amount=map.get("amount");
			orderAmount=map.get("amount");
		}
	}

	@InjectHttpOk(value = { 1, 2, 3, 4, 5 })
	public void resultOk(ResponseEntity r) {
		HashMap<String, String> map = null;
		if(ResultParse.isResultOK(r, this)){
			map = Handler_Json.JsonToCollection(r.getContentAsString());
			switch (r.getKey()) {
			case 1://获取订单信息
				
				setIntiMain(map);
				break;
			case 2://扣款计算
				if (null != map && "1".equals(map.get("resultFlg"))) {
					amount=map.get("planAmount");
					tx_price.setText(getString(R.string._amount,
							map.get("planAmount")));
					tx_discount.setText(getString(R.string._amount,
							map.get("discountAmount")));
				}
				break;
			case 3://扣款计算
				if (null != map && "1".equals(map.get("resultFlg"))) {
					tx_price.setText(getString(R.string._amount,
							map.get("planAmount")));
					amount=map.get("planAmount");
					tx_discount.setText(getString(R.string._amount,
							map.get("discountAmount")));
				}
				break;
			case 4://余额支付
				if(PAY_SUCCESS.equals(map.get("payStatus"))){
					HashMap<String, String> m=Handler_Json.jsonToList("sellerInfo", r.getContentAsString());
					Intent intent=new Intent();
					intent.setClass(this, PaySuccessActivity.class);
					intent.putExtra("merchantName", m.get("businessUserName"));
					intent.putExtra("price", amount);
					intent.putExtra("title", "支付成功");
					startActivity(intent);
					//Toast.makeText(this, "使用账户 支付成功", Toast.LENGTH_SHORT).show();
				}
				break;
			case 5://卡支付
				if(PAY_SUCCESS.equals(map.get("payStatus"))){
					Toast.makeText(this, "使用卡 支付成功", Toast.LENGTH_SHORT).show();
					HashMap<String, String> m=Handler_Json.jsonToList("sellerInfo",r.getContentAsString());
					Intent intent=new Intent();
					intent.setClass(this, PaySuccessActivity.class);
					intent.putExtra("merchantName", m.get("businessUserName"));
					intent.putExtra("price", amount);
					intent.putExtra("title", "支付成功");
					startActivity(intent);
				}
				break;
			default:
				break;
			}
		}
		dismissDialog();
	}

	@InjectHttpErr(value = { 1, 2, 3 ,4,5})
	public void resultErr(ResponseEntity r) {
		dismissDialog();
		Toast.makeText(this, ErrorMsgEnum.NET_ERROR.getName(),
				Toast.LENGTH_SHORT).show();
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
		android.net.wifi.WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
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
		jsonParams.addProperty("payOrderNo", orderno+"");
		jsonParams.addProperty("payType", 2+"");
		jsonParams.addProperty("payAmount", amount);
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
		if(ResultParse.isResultOK(r, this)){
			HashMap<String, String> map = (HashMap<String, String>) Handler_Json.JsonToCollection(r.getContentAsString());
			map.put("payOrderNo", orderno+"");
			map.put("payAmount", amount);
			new PayActivity1(this, map);
			dismissDialog();
		}else{
			Ioc.getIoc().getLogger().i("正在调取微信端服务 异常。。。。");
			dismissDialog();
		}
	}
}

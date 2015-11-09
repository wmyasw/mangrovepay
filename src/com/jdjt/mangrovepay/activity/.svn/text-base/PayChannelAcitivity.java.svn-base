package com.jdjt.mangrovepay.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.android.pc.ioc.view.listener.OnItemClick;
import com.android.pc.util.Handler_Json;
import com.google.gson.JsonObject;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.adpater.ChannelPayAdapter;
import com.jdjt.mangrovepay.application.MangrovePayApp;
import com.jdjt.mangrovepay.common.BaseFragmentActivity;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.ResultParse;

/*************************************************
 * @copyright:bupt
 * @author: 王明雨
 * @date:2015-1-19
 * @Description:TODO (选择支付渠道)
 **************************************************/
@InjectLayer(value = R.layout.acitivity_list, parent = R.id.center_common, isTitle = true)
public class PayChannelAcitivity extends BaseFragmentActivity {
	@InjectView(R.id.lv_list)
	ListView lv_list;
	String payOrderNo;
	String channelCode;
	ArrayList<HashMap<String, String>> datalist;
	Intent intent;
	ChannelPayAdapter adapter;
	String cardNo;
	int num=0;
	@InjectInit
	private void init() {
		textview_title.setText("支付方式");
		intent = new Intent();
		setResult(RESULT_OK, intent); // 这理有2个参数(int resultCode, Intent intent)
		payOrderNo = getIntent().getStringExtra("payOrderNo");
		datalist = new ArrayList<HashMap<String, String>>();
		channelCode = getIntent().getStringExtra("channelCode");
		showDialog("正在加载...");
		getPayChannel();
		setAdpater();
	}

	/*************************************************
	 @Title: getPayChannel 
	 @Description: TODO(获取支付渠道 支付类型)     设定文件 
	 @return void    返回类型 
	 @throws 
	 @date  2015-1-21
	*************************************************/
	private void getPayChannel() {
		String url = Url.DJB_PAY_CHANNEL_ARRAY;
		JsonObject jsonParams = new JsonObject();
		jsonParams.addProperty("payOrderNo", payOrderNo);
		config=new InternetConfig();
		config.setHead(CommonUtils.inHeaders());
		config.setKey(9);
		FastHttpHander.ajaxString(url, jsonParams.toString(), config, this);
	}

	/*************************************************
	 @Title: getPayCard 
	 @Description: TODO(获取绑定的卡)     设定文件 
	 @return void    返回类型 
	 @throws 
	 @date  2015-1-21
	*************************************************/
	private void getPayCard() {
		String url = Url.CARD_INFO_ARRAY;
		JsonObject jsonParams = new JsonObject();
		jsonParams.addProperty("cardKind", 2);
		config=new InternetConfig();
		config.setKey(10);
		config.setHead(CommonUtils.inHeaders());
		FastHttpHander.ajaxString(url, jsonParams.toString(), config, this);
	}

	/*************************************************
	 @Title: getPayBalance 
	 @Description: TODO(获取当前开启的账户)     设定文件 
	 @return void    返回类型 
	 @throws 
	 @date  2015-1-21
	*************************************************/
	private void getPayBalance() {
		String url = Url.MEM_INFO_ARRAY;
		JsonObject json = new JsonObject();
		json.addProperty("account", MangrovePayApp.app.getData("account") + "");
		json.addProperty("subType", "");
		json.addProperty("includeSubAcc", "1");
		config=new InternetConfig();
		config.setKey(11);
		config.setHead(CommonUtils.inHeaders());
		FastHttpHander.ajaxString(url, json.toString(), config,this);
	}
	/*************************************************
	 * @Title: resultOK
	 * @Description: TODO(支付渠道返回)
	 * @param r
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-20
	 *************************************************/
	@InjectHttpOk(value = { 9, 10, 11 })
	public void resultOK(ResponseEntity r) {
		num++;//用于计算 调用接口数，目前暂时无用 调用djb 整合接口后 该属性失效
		try {
			switch (r.getKey()) {
			case 9:
				if (ResultParse.isResultOK(r, this)) {
					ArrayList<HashMap<String, String>> list = Handler_Json
							.jsonToList("cardList",
									r.getContentAsString());
					ArrayList<HashMap<String, String>> accountSubList = Handler_Json
							.jsonToList("accountSubList",
									r.getContentAsString());
					ArrayList<HashMap<String, String>> thirdList = Handler_Json
							.jsonToList("thirdList",
									r.getContentAsString());
					if(null!=list&&list.size()>0)
						intCardData(list);
					if(null!=accountSubList&&accountSubList.size()>0){
						cardNo=MangrovePayApp.app.getData("virtualAccountNo");
						intBalanceData(accountSubList);
					}
					if(null!=thirdList&&thirdList.size()>0)
						intData(thirdList);
				}
				break;
			case 10:
				if (ResultParse.isResultOK(r, this)) {
					ArrayList<HashMap<String, String>> list = Handler_Json
							.jsonToList("list", r.getContentAsString());
					intCardData(list);
				}
				break;
			case 11:
				if (ResultParse.isResultOK(r, this)) {
					ArrayList<HashMap<String, String>> list = Handler_Json
							.jsonToList("subList", r.getContentAsString());
					HashMap<String,String> map=Handler_Json.JsonToCollection(r.getContentAsString());
					cardNo=map.get("virtualAccountNo");
					if(null!=list&&list.size()>0){
						intBalanceData(list);
					}
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			dismissDialog();
		}
		dismissDialog();
		adapter.notifyDataSetChanged();
		resetData(datalist);//为了同步刷新
//		if(num==3){
//		//	Toast.makeText(this, "执行 统一调用成功："+num, Toast.LENGTH_SHORT).show();
//			dismissDialog();
//			adapter.notifyDataSetChanged();
//			resetData(datalist);//为了同步刷新
//		}
	
	}


	public void setAdpater() {
		adapter = new ChannelPayAdapter(lv_list, datalist,
				R.layout.acitivity_pay_channel_item);
		lv_list.setAdapter(adapter);
	}

	/*************************************************
	 * @Title: intData
	 * @Description: TODO(储值卡 信息 设置初始化数据)
	 * @param list
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-19
	 *************************************************/
	public void intCardData(ArrayList<HashMap<String, String>> list) {
		HashMap<String, String> m;
		for (HashMap<String, String> map : list) {
			m = new HashMap<String, String>();
			if (channelCode.equals(map.get("subType"))) {
				m.put("img_ischecked",channelCode);
			}
			m.put("tx_channel_code", map.get("subType"));
			m.put("tx_channel_name", map.get("cardName"));
			String lastno = map.get("cardNo").substring(
					map.get("cardNo").length() - 4);
			m.put("tx_last_cardno", getString(R.string.last_cardNo, lastno));
			m.put("tx_kyed", getString(R.string._kyed_price, map.get("amount")));
			m.put("cardNo", map.get("cardNo"));
			m.put("type", "3");
			datalist.add(m);
		}
	}

	/*************************************************
	 * @Title: intData
	 * @Description: TODO(账户信息 设置初始化数据)
	 * @param list
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-19
	 *************************************************/
	public void intBalanceData(ArrayList<HashMap<String, String>> list) {
		HashMap<String, String> m;
		for (HashMap<String, String> map : list) {
			m = new HashMap<String, String>();
			if (channelCode.equals(map.get("subType"))) {
				m.put("img_ischecked", map.get("subType"));
			}
			m.put("tx_channel_code", map.get("subType"));
			m.put("tx_channel_name", map.get("subTypeName"));
			m.put("tx_kyed",
					getString(R.string._kyed_price, map.get("subMoney")));
			m.put("cardNo",cardNo);
			m.put("type", "2");
			datalist.add(m);
		}
	}

	public void resetData(ArrayList<HashMap<String, String>> list) {
		ArrayList<HashMap<String, String>> data=new ArrayList<HashMap<String,String>>();
		for (HashMap<String, String> map : list) {
			if (channelCode.equals(map.get("img_ischecked"))) {
				map.put("img_ischecked", channelCode);
			}
			data.add(map);
		}
		datalist.clear();
		datalist.addAll(data);
		adapter.notifyDataSetChanged();
	}

	/*************************************************
	 * @Title: intData
	 * @Description: TODO(设置初始化数据)
	 * @param list
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-19
	 *************************************************/
	public void intData(ArrayList<HashMap<String, String>> list) {
		HashMap<String, String> m;
		for (HashMap<String, String> map : list) {
			m = new HashMap<String, String>();
				if (channelCode.equals(map.get("code"))) {
					m.put("img_ischecked", channelCode);
				}
					m.put("tx_channel_code", map.get("code"));
					m.put("tx_channel_name", map.get("name"));
					m.put("type", "1");
					datalist.add(m);
				 //由于接口整合为一个下面接口暂时不用
				/*else if ("2".equals(map.get("type"))) {// 卡务
					if ("Epay".equals(map.get("payChannel"))) {
						getPayCard();
					}
					if ("Accountpay".equals(map.get("payChannel"))) {
						getPayBalance();
					}
				}*/
		}
	}

	@InjectMethod(@InjectListener(ids = { R.id.lv_list }, listeners = { OnItemClick.class }))
	public void OnChannelItemClick(AdapterView<?> parent, final View view,
			final int position, long id) {
		TextView channelCode = (TextView) view
				.findViewById(R.id.tx_channel_code);
		TextView channelName = (TextView) view
				.findViewById(R.id.tx_channel_name);
		ImageView img = (ImageView) view.findViewById(R.id.img_ischecked);
		/*
		 * if(img.getVisibility()==View.GONE){ img.setVisibility(View.VISIBLE);
		 * }
		 */
		this.channelCode = channelCode.getText() + "";
		resetData(datalist);
		intent.putExtra("type", datalist.get(position).get("type"));
		intent.putExtra("cardNo", datalist.get(position).get("cardNo")  + "");
		intent.putExtra("channelCode", channelCode.getText() + "");
		intent.putExtra("channelName", channelName.getText() + "");
		setResult(RESULT_OK, intent); // 这理有2个参数(int resultCode, Intent intent)
		finish();
	}

	@InjectHttpErr(value = { 9, 10, 11 })
	public void resultErr(ResponseEntity r) {
		Ioc.getIoc().getLogger().i("接口请求异常:" + r.getContentAsString());
	}

}

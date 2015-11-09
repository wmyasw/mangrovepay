package com.jdjt.mangrovepay.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.ioc.view.listener.OnItemClick;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_String;
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
@InjectLayer(value = R.layout.acitivity_consumption_list, parent = R.id.center_common, isTitle = true)
public class TransferChannelAcitivity extends BaseFragmentActivity {
	@InjectView(R.id.lv_list)
	ListView lv_list;
	@InjectView(value = R.id.null_text)
	TextView null_text;
	String payOrderNo;
	String channelCode;
	ArrayList<HashMap<String, String>> datalist;
	Intent intent;
	ChannelPayAdapter adapter;
	int num = 0;
	String cardNo;
	@InjectInit
	private void init() {
		textview_title.setText("选择转出账户");
		intent=new Intent();
		//this.setResult(1, intent);
		datalist = new ArrayList<HashMap<String, String>>();
		channelCode = getIntent().getStringExtra("channelCode");

		getPayBalance();
		lv_list.setEmptyView(null_text);
		setAdpater();
	}

	/*************************************************
	 * @Title: getPayBalance
	 * @Description: TODO(获取当前开启的账户) 设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-21
	 *************************************************/
	private void getPayBalance() {
		showDialog("正在加载...");
		String url = Url.MEM_INFO_ARRAY;
		JsonObject json = new JsonObject();
		json.addProperty("account", MangrovePayApp.app.getData("account") + "");
		json.addProperty("subType", "");
		json.addProperty("includeSubAcc", "1");
		config = new InternetConfig();
		config.setKey(11);
		config.setHead(CommonUtils.inHeaders());
		FastHttpHander.ajaxString(url, json.toString(), config, this);
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
		try {
			if (ResultParse.isResultOK(r, this)) {
				ArrayList<HashMap<String, String>> list = Handler_Json
						.jsonToList("subList", r.getContentAsString());
				
				HashMap<String,String> map=Handler_Json.JsonToCollection(r.getContentAsString());
				cardNo=map.get("virtualAccountNo");
				if (null != list && list.size() > 0) {
					intBalanceData(list);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			dismissDialog();
		}

		dismissDialog();
	}

	public void setAdpater() {
		adapter = new ChannelPayAdapter(lv_list, datalist,
				R.layout.acitivity_pay_channel_item);
		lv_list.setAdapter(adapter);
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
			m.put("cardNo", cardNo);
			m.put("tx_channel_name", "账户余额-" + map.get("subTypeName"));
			m.put("tx_kyed",
					getString(R.string._kyed_price, map.get("subMoney")));
			m.put("type", "2");
			datalist.add(m);
		}
		adapter.notifyDataSetChanged();
	}

	@InjectMethod(@InjectListener(ids = { R.id.null_text }, listeners = { OnClick.class }))
	public void click(View view) {
		getPayBalance();
	}

	public void resetData(ArrayList<HashMap<String, String>> list) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
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
		intent.putExtra("cardNo", datalist.get(position).get("cardNo") + "");
		intent.putExtra("channelCode",this.channelCode + "");
		intent.putExtra("channelName", channelName.getText() + "");
		this.setResult(RESULT_OK, intent); // 这理有2个参数(int resultCode, Intent intent)
		this.finish();
	}

	@InjectHttpErr(value = { 11 })
	public void resultErr(ResponseEntity r) {
		Ioc.getIoc().getLogger().i("接口请求异常:" + r.getContentAsString());
	}

}

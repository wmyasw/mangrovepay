package com.jdjt.mangrovepay.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.inject.InjectHttpErr;
import com.android.pc.ioc.inject.InjectHttpOk;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.inject.InjectListener;
import com.android.pc.ioc.inject.InjectMethod;
import com.android.pc.ioc.inject.InjectPullRefresh;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.internet.FastHttpHander;
import com.android.pc.ioc.internet.ResponseEntity;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.ioc.view.listener.OnItemClick;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_String;
import com.google.gson.JsonObject;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.adpater.ConsumptionPayAdapter;
import com.jdjt.mangrovepay.common.BaseFragmentActivity;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.PageUtil;
import com.jdjt.mangrovepay.utils.ResultParse;

/*************************************************
 * @copyright:bupt
 * @author: 王明雨
 * @date:2015-1-13
 * @Description:TODO (绑定卡消费详情)
 **************************************************/
@InjectLayer(value = R.layout.acitivity_consumption_list, parent = R.id.center_common, isTitle = true)
public class ConsumptionListActivity extends BaseFragmentActivity {
	@InjectView(value = R.id.lv_list, down = true, pull = true)
	ListView lv_list;
	// InternetConfig config;
	private String cardNo;
	private String subType;
	private int pageNo;
	PageUtil pageUtil;// 分页
	ArrayList<HashMap<String, String>> datalist;
	@InjectView(value = R.id.null_layout)
	LinearLayout null_layout;

	/*
	 * @InjectView(value = R.id.scrollView1, down = true,pull=true) ScrollView
	 * scrollView1;
	 */

	private ConsumptionPayAdapter adapter;

	@InjectInit
	public void init() {
		pageUtil = new PageUtil();
		datalist = new ArrayList<HashMap<String, String>>();
		textview_title.setText("交易记录");
		cardNo = getIntent().getStringExtra("cardNo");
		subType = getIntent().getStringExtra("subType");
		pageNo = 1;
		lv_list.setEmptyView(null_layout);
		setAdpater();
		getConsumptionList();
		
	}

	@InjectMethod(@InjectListener(ids = { R.id.img_null_data }, listeners = { OnClick.class }))
	public void click(View view) {
		getConsumptionList();
	}

	private void getConsumptionList() {
		showDialog();
		String url = Url.METHOD_CONSUMEINFO_ARRAY;
		config.setKey(1);
		config.setHead(CommonUtils.inHeaders());
		JsonObject json = new JsonObject();
		json.addProperty("cardNo", cardNo);
		json.addProperty("cardType", subType);
		json.addProperty("startDate", "");
		json.addProperty("endDate", "");
		json.addProperty("pageNo", pageNo);
		json.addProperty("pageCount", "10");
		FastHttpHander.ajaxString(url, json.toString(), config, this);
	}

	@InjectMethod(@InjectListener(ids = { R.id.lv_list }, listeners = { OnItemClick.class }))
	public void OnChannelItemClick(AdapterView<?> parent, final View view,
			final int position, long id) {
	}

	@InjectHttpOk(1)
	private void resultOk(ResponseEntity r) {
		dismissDialog();
		if (ResultParse.isResultOK(r, this)) {
			ArrayList<HashMap<String, String>> list = Handler_Json.jsonToList(
					"list", r.getContentAsString());
			String page = (String) Handler_Json.getValue("pageNo", r.getContentAsString());
			if (!Handler_String.isBlank(page)) {
				pageNo = Integer.valueOf(page) + 1;
			}
			initData(list,page);
		}
	}

	@InjectPullRefresh
	public void callListView(int type) {
		// 这里的type来判断是否是下拉还是上拉
		switch (type) {
		case InjectView.DOWN:
			pageNo = 1;
			getConsumptionList();
			pageUtil.isEndDownPage();
			break;
		case InjectView.PULL:
			pageUtil.isEndPullPage();// 设置向下翻页
			getConsumptionList();
			break;
		}

	}

	private void initData(ArrayList<HashMap<String, String>> list, String page) {
		HashMap<String, String> m = null;
		if("1".equals(page)){
			datalist.clear();
		}
		ArrayList<HashMap<String, String>> datas=new ArrayList<HashMap<String,String>>();
		for (HashMap<String, String> map : list) {
			m = new HashMap<String, String>();
			m.put("tx_type", map.get("recordTypeName"));
			m.put("tx_time", map.get("costDate"));
			m.put("tx_consumption", getString(R.string._amount,CommonUtils.fomatFloat2(Float.valueOf(map.get("unitCost")))));
			datalist.add(m);
		}
		datalist.addAll(datas);
		adapter.notifyDataSetChanged();
	}

	private void setAdpater() {
		adapter = new ConsumptionPayAdapter(lv_list, datalist,
				R.layout.acitivity_transaction_item);
		lv_list.setAdapter(adapter);
	}

	@InjectHttpErr(value = { 1 })
	public void resultErr(ResponseEntity r) {
		dismissDialog();
		Ioc.getIoc().getLogger().i("接口请求异常:" + r.getContentAsString());
	}
}

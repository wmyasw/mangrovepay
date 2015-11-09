package com.jdjt.mangrovepay.fragement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectHttpErr;
import com.android.pc.ioc.inject.InjectHttpOk;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectListener;
import com.android.pc.ioc.inject.InjectMethod;
import com.android.pc.ioc.inject.InjectPullRefresh;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.internet.FastHttpHander;
import com.android.pc.ioc.internet.InternetConfig;
import com.android.pc.ioc.internet.ResponseEntity;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.ioc.view.listener.OnItemClick;
import com.android.pc.util.Handler_Inject;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_String;
import com.google.gson.JsonObject;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.activity.WebViewAactiviy;
import com.jdjt.mangrovepay.adpater.MSGInfoAdapter;
import com.jdjt.mangrovepay.application.MangrovePayApp;
import com.jdjt.mangrovepay.common.BaseFragment;
import com.jdjt.mangrovepay.contanst.Constants;
import com.jdjt.mangrovepay.contanst.ErrorMsgEnum;
import com.jdjt.mangrovepay.contanst.HeaderConst;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.event.MSGInfoEntity;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.DateUtil;
import com.jdjt.mangrovepay.utils.PageUtil;
import com.jdjt.mangrovepay.utils.ResultParse;

/*************************************************
 * @copyright:bupt
 * @author: 王明雨
 * @date:2015-1-9
 * @Description:TODO (消息中心)
 **************************************************/
public class MSGInfoFragment extends BaseFragment {
	EventBus eventBus = EventBus.getDefault();

	@InjectView(value = R.id.lv_list, down = true, pull = true)
	public ListView lv_list;
	private String cardNo;
	private String subType;
	private int pageNo=1;
	PageUtil pageUtil;// 分页
	ArrayList<HashMap<String, String>> datalist;
	@InjectView(value = R.id.null_layout)
	public LinearLayout null_layout;
	MSGInfoAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View rootView = inflater.inflate(R.layout.acitivity_consumption_list, container,
				false);
		Handler_Inject.injectFragment(this, rootView);
		return rootView;
	}

	@InjectInit
	public void init() {
		MSGInfoEntity msg=new MSGInfoEntity();
		eventBus.post(msg);
		lv_list.setEmptyView(null_layout);
		datalist=new ArrayList<HashMap<String,String>>();
		pageUtil=new PageUtil();
		//getMsgTime();
		setAdpater();
		showDialog();
		getMsgList();
		
	}

	/*************************************************
	 @Title: click 
	 @Description: TODO(点击空数据页面 加载信息) 
	 @param view    设定文件 
	 @return void    返回类型 
	 @throws 
	 @date  2015-2-6
	*************************************************/
	@InjectMethod(@InjectListener(ids = { R.id.img_null_data }, listeners = { OnClick.class }))
	public void click(View view) {
		getMsgList();
	}
	@InjectMethod(@InjectListener(ids = { R.id.lv_list }, listeners = { OnItemClick.class }))
	public void onItemClick(AdapterView<?> parent, View view,
			int position, long id){
		TextView text_url=(TextView) view.findViewById(R.id.tx_content_url);
		Intent intent =new Intent().setClass(activity, WebViewAactiviy.class);
		intent.putExtra("url",text_url.getText()+"");
		intent.putExtra("title","消息详情");
		activity.startActivity(intent);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		eventBus.unregister(this);
	}

	@InjectPullRefresh
	public void callListView(int type) {
		// 这里的type来判断是否是下拉还是上拉
		switch (type) {
		case InjectView.DOWN:
			pageNo = 1;
			getMsgList();
			break;
		case InjectView.PULL:
			getMsgList();
			break;
		}

	}
	
	/*************************************************
	 @Title: getMsgList 
	 @Description: TODO(获取消息列表)     设定文件 
	 @return void    返回类型 
	 @throws 
	 @date  2015-2-6
	*************************************************/
	private void getMsgList(){
		//showDialog();
		String url=Url.SERVICE_MSG_ARRAY_URL;
		JsonObject json=new JsonObject();
		json.addProperty("pageNo", pageNo);
		json.addProperty("pageCount", 10);
		json.addProperty("msgTime", "");
		InternetConfig config=new InternetConfig();
		config.setKey(1);
		config.setHead(CommonUtils.inHeaders());
		FastHttpHander.ajaxString(url, json.toString(), config, this);
	}
	@InjectHttpOk(1)
	public void resultOk(ResponseEntity r){
		dismissDialog();
		if(ResultParse.isResultOK(r, activity)){
			ArrayList<HashMap<String,String>> list=Handler_Json.jsonToList("list", r.getContentAsString());
			String page = (String) Handler_Json.getValue("pageNo", r.getContentAsString());
			String time= (String) Handler_Json.getValue("sysDateTime", r.getContentAsString());
			MangrovePayApp.msgTime=time;
			MangrovePayApp.app.setData("msgTime", time);
			Ioc.getIoc().getLogger().i("-------------------------");
			Ioc.getIoc().getLogger().i("-------------系统时间："+	MangrovePayApp.msgTime);
			if (!Handler_String.isBlank(page)) {
				pageNo = Integer.valueOf(page) + 1;
			}
			initData(list,page);
		}
	}
	@InjectHttpErr(value={1,3})
	public void resultErr(ResponseEntity r){
		dismissDialog();
		Ioc.getIoc().getLogger().i("接口请求异常:" + r.getContentAsString());
		if(r.getKey()==1){
			pageUtil.isEndDownPage();
			Toast.makeText(activity, ErrorMsgEnum.NET_ERROR.getName(),
					Toast.LENGTH_SHORT).show();
		}
		
	}
//	private void getMsgTime(){
//		String url=Url.SERVICE_SYSDATE_URL;
//		InternetConfig config=new InternetConfig();
//		config.setKey(3);
//		config.setHead(CommonUtils.inHeaders());
//		FastHttpHander.ajaxString(url, "", config, this);
//	}
//	@InjectHttpOk(value={3})
//	public void getMsgTimeOK(ResponseEntity r){
//		try {
//			Map<String, String> map = r.getHeaders();
//			// 获取头部信息
//		//	String[] msgArray = map.get(HeaderConst.MYMHOTEL_MESSAGE).split("\\|");
//			// 成功 返回OK
//			if ("OK".equals(map.get(HeaderConst.MYMHOTEL_STATUS))) {
//				// Toast.makeText(context, msgArray[1], Toast.LENGTH_SHORT).show();
//				String time= (String) Handler_Json.getValue("sysDateTime", r.getContentAsString());
//				MangrovePayApp.msgTime=time;
//				MangrovePayApp.app.setData("msgTime", time);
//				Ioc.getIoc().getLogger().i("-------------------------");
//				Ioc.getIoc().getLogger().i("-------------系统时间："+	MangrovePayApp.msgTime);
//			}// 失败
//		} catch (Exception e) {
//			Ioc.getIoc().getLogger().i("-------------系统时间错误：");
//			e.printStackTrace();
//		}
//	}

	/*************************************************
	 @Title: initData 
	 @Description: TODO(组装数据) 
	 @param list
	 @param page    设定文件 
	 @return void    返回类型 
	 @throws 
	 @date  2015-2-6
	*************************************************/
	private void initData(ArrayList<HashMap<String, String>> list, String page) {
		pageUtil.isEndDownPage();
		if(null!=list&&list.size()>0){
			HashMap<String, String> m = null;
			if("1".equals(page)){
				datalist.clear();
			}
			ArrayList<HashMap<String, String>> datas=new ArrayList<HashMap<String,String>>();
			
			for (HashMap<String, String> map : list) {
				m = new HashMap<String, String>();
				m.put("tx_content_url", map.get("msgCntUrl"));
				m.put("tx_msg_title", map.get("msgTitle"));
				m.put("tx_msg_content", "");
				m.put("tx_msg_time", DateUtil.formatDateToDay(map.get("msgTime")));
				datalist.add(m);
			}
			datalist.addAll(datas);
			adapter.notifyDataSetChanged();
		}
	}
	/*************************************************
	 @Title: setAdpater 
	 @Description: TODO(初始化 listview 需要的 组件)     设定文件 
	 @return void    返回类型 
	 @throws 
	 @date  2015-2-5
	*************************************************/
	private void setAdpater() {
		
		adapter = new MSGInfoAdapter(lv_list, datalist,
				R.layout.acitivity_msg_item);
		lv_list.setAdapter(adapter);
	}

	
}

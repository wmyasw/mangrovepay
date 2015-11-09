package com.jdjt.mangrovepay.adpater;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.pc.ioc.adapter.CommonLazyAdapter;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.util.Handler_String;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.adpater.ConsumptionPayAdapter.ViewHolder;
import com.jdjt.mangrovepay.common.BaseViewHolder;

/*************************************************
@copyright:bupt
@author: 王明雨
@date:2015-1-20
@Description:TODO (支付渠道)
**************************************************/
public class ConsumptionPayAdapter extends
		CommonLazyAdapter<HashMap<String, String>, ViewHolder> {
	 private ListView listView;
	 private ArrayList<HashMap<String, String>> dataList;
	public ConsumptionPayAdapter(AbsListView listView,
			ArrayList<HashMap<String, String>> dataList, int layout_id) {
		super(listView, dataList, layout_id,0);
		// TODO Auto-generated constructor stub
		this.dataList=dataList;
		this.listView=(ListView) listView;
	}


	@Override
	public void deal(HashMap<String, String> data, ViewHolder viewHold,
			int position) {
		// TODO Auto-generated method stub
		injectAdapter( viewHold, position);
	}
	/**
	 * adapter里面使用 自动去注入组件
	 * @author mars 2013-10-22 下午12:59:07
	 * @param object
	 * @param view
	 * @return void
	 */
	private void injectAdapter(Object view, int position) {
		String data;
		Field[] fields = view.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				InjectView v = field.getAnnotation(InjectView.class);
				if (v == null) {
					continue;
				}
				data = getString(position, field.getName());
				if (data == null) {
					continue;
				}
				try {
					field.setAccessible(true);
					Object object = field.get(view);
					if (object == null || !View.class.isAssignableFrom(object.getClass())) {
						continue;
					}
					((View)object).setTag(position);
					if (TextView.class.isAssignableFrom(object.getClass())) {
						if("tx_kyed".equals(field.getName())){
							if(!Handler_String.isBlank(dataList.get(position).get("tx_kyed"))){
								((TextView) object).setVisibility(View.VISIBLE);
							}else{
								((TextView) object).setVisibility(View.GONE);
							}
						}
						
						((TextView) object).setText(data);
					}
					if (ImageView.class.isAssignableFrom(object.getClass())) {
						if("img_ischecked".equals(field.getName())){
							if(!Handler_String.isBlank(dataList.get(position).get("img_ischecked"))){
								((ImageView) object).setVisibility(View.VISIBLE);
							}else{
								((ImageView) object).setVisibility(View.GONE);
							}
						}else{
							//if((dataList.get(position).get("hotel_card_image")).equals("1"))
							((ImageView) object).setImageResource(Integer.parseInt(data.toString()));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public class ViewHolder extends BaseViewHolder{
		/*************************************************
		Copyright:bupt
		
		@author: 王明雨
		@date:2015-3-23:2010-08-25
		@Description:描述主要实现的功能
		**************************************************/
		
		private static final long serialVersionUID = 2066076498671510536L;
		@InjectView(R.id.tx_type)
		public TextView tx_type;
		@InjectView(R.id.tx_channel_code)
		public TextView tx_channel_code;
//		@InjectView(R.id.img_ischecked)
//		ImageView img_ischecked;
		@InjectView(R.id.tx_time)
		public TextView tx_time;
		@InjectView(R.id.tx_last_cardno)
		public TextView tx_last_cardno;
		@InjectView(R.id.tx_consumption)
		public TextView tx_consumption;
		
	}


}

package com.jdjt.mangrovepay.adpater;

import java.util.ArrayList;
import java.util.HashMap;

import android.widget.AbsListView;
import android.widget.TextView;

import com.android.pc.ioc.adapter.CommonLazyAdapter;
import com.android.pc.ioc.inject.InjectView;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.adpater.MSGInfoAdapter.ViewHolder;
import com.jdjt.mangrovepay.common.BaseViewHolder;

/*************************************************
@copyright:bupt
@author: 王明雨
@date:2015-1-20
@Description:TODO (消息中心 列表适配器)
**************************************************/
public class MSGInfoAdapter extends
		CommonLazyAdapter<HashMap<String, String>, ViewHolder> {
	public MSGInfoAdapter(AbsListView listView,
			ArrayList<HashMap<String, String>> dataList, int layout_id) {
		super(listView, dataList, layout_id,0);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void deal(HashMap<String, String> data, ViewHolder viewHold,
			int position) {
		super.deal(data, viewHold, position);
	}


	public class ViewHolder extends BaseViewHolder{
		/*************************************************
		Copyright:bupt
		
		@author: 王明雨
		@date:2015-3-23:2010-08-25
		@Description:描述主要实现的功能
		**************************************************/
		
		private static final long serialVersionUID = 5481780654816377169L;
		@InjectView(R.id.tx_msg_time)
		public TextView tx_msg_time;
//		@InjectView(R.id.img_ischecked)
//		ImageView img_ischecked;
		@InjectView(R.id.tx_msg_content)
		public TextView tx_msg_content;
		@InjectView(R.id.tx_msg_title)
		public TextView tx_msg_title;
		@InjectView(R.id.tx_content_url)
		public TextView tx_content_url;
		
	}


}

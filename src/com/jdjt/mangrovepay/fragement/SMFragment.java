package com.jdjt.mangrovepay.fragement;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.util.Handler_Inject;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.adpater.HotelBalanceAdapter;
import com.jdjt.mangrovepay.common.BaseFragment;

/*************************************************
 * @copyright:bupt
 * @author: 王明雨
 * @date:2015-1-9
 * @Description:TODO 
 **************************************************/
public class SMFragment extends BaseFragment {
	EventBus eventBus = EventBus.getDefault();

	ArrayList<HashMap<String, String>> mapList = null;
	HotelBalanceAdapter adapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View rootView = inflater.inflate(R.layout.fragement_sm, container,
				false);
		Handler_Inject.injectFragment(this, rootView);
		return rootView;
	}

	@InjectInit
	public void init() {

		// getAccountInfo();
	}


}

package com.jdjt.mangrovepay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.jdjt.mangrovepay.R;

;
public class ListFooterView extends View  {
	private Context context;
	AttributeSet attrs;
	View view;
	
	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public ListFooterView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public ListFooterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		this.attrs=attrs;
		init();
	}

	public ListFooterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.attrs=attrs;
		init();
	}

	private void init(){
		LayoutInflater inflater1 = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View views= inflater1.inflate(R.layout.fragement_add_card, null);// 填充view
		setView(views);
	}

}
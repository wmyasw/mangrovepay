package com.jdjt.mangrovepay.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.android.pc.util.Handler_String;
import com.jdjt.mangrovepay.R;

;
public class ClearEditText extends EditText implements
		OnFocusChangeListener {
	private Drawable imgEnable;
	private Context context;
	AttributeSet attrs;

	public ClearEditText(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		this.attrs=attrs;
		
		init();
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.attrs=attrs;
		init();
	}

	private void init() {
		// 获取图片资源
		imgEnable = context.getResources().getDrawable(R.drawable.ed_clear);
		addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				setDrawable();
				//Toast.makeText(context, getText(), 10).show();
			}
		});
		
		setCompoundDrawablePadding(10);
		setDrawable();
	}

	/**
	 * 设置删除图片
	 */
	private void setDrawable() {
		Drawable[] dr=getCompoundDrawables();
		if (!Handler_String.isEmpty(this.getText()+"")&&this.hasFocus()) {
				setCompoundDrawablesWithIntrinsicBounds(dr[0], dr[1], imgEnable, dr[3]);
		}else{
			setCompoundDrawablesWithIntrinsicBounds(dr[0], dr[1], null, dr[3]);
		}
	}

	/**
	 * event.getX() 获取相对应自身左上角的X坐标 event.getY() 获取相对应自身左上角的Y坐标 getWidth()
	 * 获取控件的宽度 getPaddingRight() 获取删除图标右边缘到控件右边缘的距离 getWidth() -
	 * getTotalPaddingRight() 计算删除图标左边缘到控件左边缘的距离 getWidth() - getPaddingRight()
	 * 计算删除图标右边缘到控件左边缘的距离
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (imgEnable != null && event.getAction() == MotionEvent.ACTION_UP) {
			int x = (int) event.getX();
			// 判断触摸点是否在水平范围内
			boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight()))
					&& (x < (getWidth() - getPaddingRight()));
			// 获取删除图标的边界，返回一个Rect对象
			Rect rect = imgEnable.getBounds();
			// 获取删除图标的高度
			int height = rect.height();
			int y = (int) event.getY();
			// 计算图标底部到控件底部的距离
			int distance = (getHeight() - height) / 2;
			// 判断触摸点是否在竖直范围内(可能会有点误差)
			// 触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
			boolean isInnerHeight = (y > distance) && (y < (distance + height));
			
			if (isInnerWidth && isInnerHeight) {
				setText("");
			}

		}

		return super.onTouchEvent(event);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		Drawable[] dr=getCompoundDrawables();
		// TODO Auto-generated method stub
		if (focused) {
			setDrawable();
		} else {
			setCompoundDrawablesWithIntrinsicBounds(dr[0], dr[1], null, dr[3]);
		}
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		Drawable[] dr=getCompoundDrawables();
		if (hasFocus) {
			setDrawable();
		} else {
			setCompoundDrawablesWithIntrinsicBounds(dr[0], dr[1], null, dr[3]);
		}
	}

}
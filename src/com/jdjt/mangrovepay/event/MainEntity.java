package com.jdjt.mangrovepay.event;

/**
 * 右部展开控制对象
 * 
 * @author gdpancheng@gmail.com 2013-10-28 上午10:05:43
 */
public class MainEntity {

	private boolean isViewEnable = false;
	private String title_name;
	
	public String getTitle_name() {
		return title_name;
	}

	public void setTitle_name(String title_name) {
		this.title_name = title_name;
	}

	public boolean isViewEnable() {
		return isViewEnable;
	}

	public void setViewEnable(boolean isViewEnable) {
		this.isViewEnable = isViewEnable;
	}

	


}

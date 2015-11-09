package com.jdjt.mangrovepay.event;

import android.content.Context;
import android.view.View;


/*************************************************
@copyright:bupt
@author: 王明雨
@date:2015-1-9
@Description:TODO (Msg 消息读取实体)
**************************************************/
public class MSGEntity {

	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-2-5:2010-08-25
	@Description:是否读取，默认为 未读
	**************************************************/
	private boolean isRead=false;
	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	
	
}

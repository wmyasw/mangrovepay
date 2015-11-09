package com.jdjt.mangrovepay.contanst;

import java.util.Arrays;
import java.util.List;

public enum CardTypeEnum {
	
	YZK("0","业主卡"),
	DJK("1","度假卡"),
	CZL("2","储值卡");
	private String code;
	private String name;
	private CardTypeEnum(String code,String name) {
		this.code = code;
		this.name=name;
	}
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}




	/** 
	* @Title: getAll 
	* @Description: TODO(获得所有的对象数据) 
	* @param @return    设定文件 
	* @return List<ModelEnum>    返回类型 
	* @throws 
	* @author 王明雨
	* @date 2013-5-30 上午09:58:31 
	*/ 
	public static List<CardTypeEnum> getAll() {
		return Arrays.asList(CardTypeEnum.values());
	}

	
	/** 
	* @Title: getTransactionTypesEnum 
	* @Description: TODO(根据code值获得对应的渠道类型) 
	* @param @param code
	* @param @return    设定文件 
	* @return ModelEnum    返回类型 
	* @throws 
	* @author 王明雨
	* @date 2013-5-30 上午09:58:12 
	*/ 
	public static CardTypeEnum  getCardTypeEnum(String code) {
		code = code.trim();
		for (CardTypeEnum cardTypeEnum : values()) {
			if (code.equals(cardTypeEnum.getCode())) {
				return cardTypeEnum;
			}
		}
		return null;
	}
	
	
}

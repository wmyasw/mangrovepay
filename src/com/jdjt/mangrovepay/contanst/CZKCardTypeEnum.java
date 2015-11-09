package com.jdjt.mangrovepay.contanst;

import java.util.Arrays;
import java.util.List;

import com.jdjt.mangrovepay.R;

public enum CZKCardTypeEnum {
	
	GEM("8868",R.drawable.card_gem),
	EMERALD("8898",R.drawable.card_emerald),
	COMMON ("8888",0);
	private String code;
	private int name;
	private CZKCardTypeEnum(String code,int name) {
		this.code = code;
		this.name=name;
	}
	
	public int getName() {
		return name;
	}



	public void setName(int name) {
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
	public static List<CZKCardTypeEnum> getAll() {
		return Arrays.asList(CZKCardTypeEnum.values());
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
	public static CZKCardTypeEnum  getCardTypeEnum(String code) {
		code = code.trim();
		for (CZKCardTypeEnum cardTypeEnum : values()) {
			if (code.equals(cardTypeEnum.getCode())) {
				return cardTypeEnum;
			}
		}
		return null;
	}
	
	
}

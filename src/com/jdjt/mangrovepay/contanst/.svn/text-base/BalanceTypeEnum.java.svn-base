package com.jdjt.mangrovepay.contanst;

import java.util.Arrays;
import java.util.List;

import com.jdjt.mangrovepay.R;

public enum BalanceTypeEnum {
	
	ACCOUNT_7("10",R.drawable.discount_bg),
	ACCOUNT_8("11",R.drawable.discount_8_bg),
	ACCOUNT_9("12",R.drawable.discount_9_bg),
	ACCOUNT_95("13",R.drawable.discount_95_bg),
	DEFULT("defult",R.drawable.discount_bg);
	private String code;
	private int layout_id;
	private BalanceTypeEnum(String code,int layout_id) {
		this.code = code;
		this.layout_id=layout_id;
	}
	


	public int getLayout_id() {
		return layout_id;
	}



	public void setLayout_id(int layout_id) {
		this.layout_id = layout_id;
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
	public static List<BalanceTypeEnum> getAll() {
		return Arrays.asList(BalanceTypeEnum.values());
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
	public static BalanceTypeEnum  getCardTypeEnum(String code) {
		code = code.trim();
		for (BalanceTypeEnum cardTypeEnum : values()) {
			if (code.equals(cardTypeEnum.getCode())) {
				return cardTypeEnum;
			}
		}
		return DEFULT;
	}
	
	
}

package com.jdjt.mangrovepay.contanst;

import java.util.Arrays;
import java.util.List;

public enum ErrorMsgEnum {
	
	NET_ERROR("10","网络连接不畅，请稍后再试"),
	NET_NOT_CONNECT("11","网络无法连接"),
	OPERATION_ERROR("20","抱歉操作无效，请稍后在试"),
	PHONE_ERROR("31","请输入正确的手机号"),
	TICKET_ISNULL("TICKET_ISNULL","用户未登录"),
	CHECK_CONTINUE("CHECK_CONTINUE","验证点击过于频繁,请一分钟后再重新验证"),
	SYSTEM_EXCEPTION("sys_exception","系统异常"),
	SCANQRCODE_ERR("TOKEN_ERR","二维码无法解析或不存在"),
	LOGIN_ERR("UNLOGIN","当前登录状态失效"),
	SCANQRCODE_TOKEN_INVALID("TOKEN_INVALID","二维码无效"),
	CARD_ERR_1("001","无效的红树林卡"),
	CARD_ERR_2("002","卡账户不存在"),
	CARD_ERR_3("003","卡登陆密码不正确"),
	CARD_ERR_4("004","该卡不属于当前登录的用户"),
	CARD_ERR_5("005","卡交易密码不正确"),
	CARD_ERR_6("006","无法获取度假卡信息"),
	CARD_ERR_7("007","用户所属卡信息为空"),
	CARD_ERR_8("008","卡位数不足"),
	CARD_ERR_9("009","不能绑定度假卡"),
	CARD_ERR_10("010","此卡已绑定，不能重复绑定"),
	CARD_ERR_11("011","卡解绑失败"),
	MEBER_ERR_21("021","输入的会员中行号不存在"),
	PHONE_ERR_30("030","手机号已绑定"),
	JW_ERR_CODE_1("TS001-1000","卡账户不存在"),
	JW_ERR_CODE_2("TS001-1005","错误的有效期"),
	JW_ERR_CODE_3("TS001-1101","卡账户状态错误"),
	JW_ERR_CODE_4("TS001-1117","卡号不存在"),
	JW_ERR_CODE_5("TS001-1126","卡状态错误"),
	JW_ERR_CODE_6("TS001-1127","卡类型错误"),
	JW_ERR_CODE_7("TS001-1137","可申请卡数量不足"),
	JW_ERR_CODE_8("TS001-1140","卡子账户不存在"),
	JW_ERR_CODE_9("TS001-1141","无记录"),
	JW_ERR_CODE_11("TS001-1150","无效的度假卡"),
	JW_ERR_CODE_12("TS001-1151","无效的度假卡"),
	JW_ERR_CODE_13("TS001-1401","无效的度假卡"),
	JW_ERR_CODE_14("TS001-2504","无效的交易数据"),
	JW_ERR_CODE_15("TS001-2505","无效的交易数据"),
	JW_ERR_CODE_16("TS001-2506","无效的交易数据"),
	JW_ERR_CODE_17("TS001-2510","无效的度假卡"),
	JW_ERR_CODE_18("TS001-2511","无效商户"),
	JW_ERR_CODE_19("TS001-2514","无效卡信息"),
	JW_ERR_CODE_21("TS001-2517","无效卡信息"),
	JW_ERR_CODE_22("TS001-2518","无效卡信息"),
	JW_ERR_CODE_23("TS001-2519","无效卡信息"),
	JW_ERR_CODE_24("TS001-2601","卡账户校验异常"),
	JW_ERR_CODE_25("TS001-2602","卡账户异常"),
	JW_ERR_CODE_26("TS001-2603","卡账户不存在"),
	JW_ERR_CODE_27("TS001-2605","卡账户状态无效"),
	JW_ERR_CODE_28("TS001-2612","子账户余额不足"),
	JW_ERR_CODE_29("TS001-2804","商户没有权限"),
	JW_ERR_CODE_30("TS001-3001","输入密码不正确"),
	JW_ERR_CODE_31("TS001-3002","密码输入错误次数超限"),

	JW_ERR_CODE_135("TS001-3102","初始密码请修改"),
	JW_ERR_CODE_132("TS001-8010","虚拟卡计算充值账户与上送账户不一致"),
	JW_ERR_CODE_133("TS001-8012","虚拟卡退款失败 "),
	JW_ERR_CODE_134("TS001-8011","虚拟卡无可充值账户"),
	JW_ERR_CODE_136("TS001-8013","支付流水号重复,不允许重复提交"),
	JW_ERR_CODE_137("TS001-8014","支付交易实付金额与支付核算实付金额不一致"),
	
	JW_ERR_CODE_139("TS001-8015","当前商户不支持该折扣账户消费"),
	JW_ERR_CODE_140("TS001-8016","单笔消费超限"),
		JW_ERR_CODE_999("TS001-9997","网络连接异常，请稍候在试");
	
	
	
	
	


	private String code;
	private String name;
	
	private ErrorMsgEnum(String code,String name) {
		this.code = code;
		this.name = name;
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
	public static List<ErrorMsgEnum> getAll() {
		return Arrays.asList(ErrorMsgEnum.values());
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
	public static ErrorMsgEnum  getErrorMsgEnum(String code) {
		code = code.trim();
		for (ErrorMsgEnum errorMsgEnum : values()) {
			if (code.equals(errorMsgEnum.getCode())) {
				return errorMsgEnum;
			}
		}
		return null;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}

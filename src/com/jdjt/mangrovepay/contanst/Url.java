package com.jdjt.mangrovepay.contanst;




/**
 * <P>
 * Description: TODO
 * </P>
 * 
 * @ClassName: Url
 * @author csj 2013-7-18 上午10:00:48
 */
public interface Url {

	/**
	 * 服务地址 2014-5-27上午10:15:44
	 * 
	 * @author wangmingyu
	 */


	String BASE = "http://192.168.1.36:8080/";// 服务器
//	String BASE = "http://rc-ws.mymhotel.com/";// 服务器内网
	String BASEHTTPS = "http://rc-ws.mymhotel.com/";// 服务器 内网https

//	String BASE = "http://192.168.10.2:8080/";// 服务器
//	String BASEHTTPS = "http://192.168.10.2:8080/";// 服务器
//	String BASE = "http://mws.mymhotel.com/";// 服务器 外网
	/*****************************红树林服务 *****************************************************/

	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-2-5:2010-08-25
	@Description:消息中心  4.3	消息列表（带分页）
	**************************************************/
	public static final String SERVICE_MSG_ARRAY_URL = BASE+ModelEnum.DJB.getCode()+"/msg/query/msg_array.json";
	
	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-2-5:2010-08-25
	@Description:消息详情
	**************************************************/
	public static final String SERVICE_MSG_DETAIL_URL = BASE+ModelEnum.DJB.getCode()+"/msg/query/msg_detail.json";
	
	public static final String SERVICE_MSG_PAY_COUNT_URL = BASE+ModelEnum.DJB.getCode()+"/msg/query/msg_count.json";
	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-3-6:2010-08-25
	@Description:消息时间
	**************************************************/
//	public static final String SERVICE_SYSDATE_URL = BASE+ModelEnum.HOTELSERVICE.getCode()+"/service/comm/sysdate";
	// ///////////////////////////////////酒店信息查询地址/////////////////////////////////////////////

	/***************************** 账户中心**开始 *****************************************************/
	/**
	 * 用户登录
	 */
	public static final String METHOD_LOGIN = BASEHTTPS + ModelEnum.UUM.getCode()
			+ "/mem/sso/login.json";

	/**
	 * 用户退出
	 */
	public static final String METHOD_LOGOUT = BASE + ModelEnum.UUM.getCode()
			+ "/mem/sso/logout.json";
	/**
	 * 用户注册
	 */
	public static final String METHOD_REG = BASEHTTPS + ModelEnum.UUM.getCode()
			+ "/mem/account/register.json";
	/**
	 * 用户账号重复验证
	 */
	public static final String MEM_CHECK_ACCOUNT = BASE + ModelEnum.UUM.getCode()
			+ "/mem/account/check_account.json";
	/**
	 * 获取用户验证码
	 */
	public static final String METHOD_GETCODE = BASE + "mymapp/djb"
			+ "/common/captcha/gain_captcha.json";

	/**
	 * 验证码验证
	 */
	public static final String METHOD_CHECKCAPTCHA = BASE
			+ ModelEnum.UUM.getCode() + "/common/captcha/check_captcha.json";

	/**
	 * 获取会员信息
	 */
	public static final String METHOD_MEMBERINFO = BASE
			+ ModelEnum.UUM.getCode() + "/mem/account/member_info.json";

	/**
	 * 会员充值
	 */
	public static final String METHOD_RECHANGE = BASEHTTPS
			+ ModelEnum.UUM.getCode() + "/mem/account/sub/recharge.json";
	/**
	 * 会员转帐
	 */
	public static final String METHOD_TRANSFER = BASEHTTPS
			+ ModelEnum.UUM.getCode() + "/mem/account/sub/transfer.json";
	/**
	 * 会员帐户余额信息查询
	 */
	public static final String METHOD_MEMBERBALANCEINFO = BASE
			+ ModelEnum.UUM.getCode() + "/mem/account/member_balance_info.json";
	/**
	 * 修改会员信息
	 */
	public static final String METHOD_MODIFYMEMBER = BASE
			+ ModelEnum.UUM.getCode() + "/mem/account/modify_member.json";

	/**
	 * 重新绑定
	 */
	public static final String METHOD_BINDING = BASEHTTPS + ModelEnum.UUM.getCode()
			+ "/mem/account/binding.json";

	/**
	 * 找回密码
	 */
	public static final String METHOD_RESETPASSWORD = BASEHTTPS
			+ ModelEnum.UUM.getCode() + "/mem/account/reset_password.json";

	/**
	 * 修改用户密码
	 */
	public static final String METHOD_MODIFYPASSWORD = BASEHTTPS
			+ ModelEnum.UUM.getCode() + "/mem/account/modify_password.json";

	/**
	 * 修改用户支付密码
	 */
	public static final String METHOD_MODIFYPAYMENTPWD = BASEHTTPS
			+ ModelEnum.UUM.getCode()
			+ "/mem/account/modify_payment_password.json";
	
	/**
	 * 会员帐户实名制
	 */
	public static final String METHOD_SETTINTBALANCE = BASEHTTPS
			+ ModelEnum.UUM.getCode()
			+ "/mem/account/setting_member_balance.json";
	
	/**
	 * 会员帐户消费明细
	 */
	public static final String METHOD_VIRTUALTOTALDETAIL = BASE
			+ ModelEnum.UUM.getCode()
			+ "/mem/account/member_virtual_totaldetail.json";
	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-1-27:2010-08-25
	@Description:找回支付密码
	**************************************************/
	public static final String METHOD_RESET_PAYMENT_PASSWORD = BASE
			+ ModelEnum.UUM.getCode()
			+ "/mem/account/reset_payment_password.json";
	
	
	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-1-20:2010-08-25
	@Description:交易明细
	**************************************************/
	public static final String MEMBER_CONSUME_ARRAY = BASE
			+ ModelEnum.UUM.getCode()
			+ "/mem/account/member_consume_array.json";
	
	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-1-16:2010-08-25
	@Description:账户充值请求
	**************************************************/
	public static final String METHOD_REQ_RECHARGE = BASE
			+ ModelEnum.UUM.getCode()
			+ "/mem/account/recharge/req_recharge.json";
	
	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-1-15:2010-08-25
	@Description:会员帐户信息查询
	**************************************************/
	public static final String MEM_INFO_ARRAY = BASE
	+ ModelEnum.UUM.getCode()
	+"/mem/account/member_account_info.json";
	
	
	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-1-23:2010-08-25
	@Description:会员转账
	**************************************************/
	public static final String MEM_SUB_TRANSFER = BASE
			+ ModelEnum.UUM.getCode()
			+"/mem/account/sub/transfer.json";
			

	/***************************** 账户中心*结束 ******************************************************/

	

	/***************************** 账户中心*结束 ******************************************************/
	/***************************** 红树林卡** 开始 ******************************************************/
	/**
	 * 红树林卡绑定
	 */
	public static final String METHOD_CARDBINDING = BASE
			+ ModelEnum.CARD.getCode()
			+ "/card/comm/binding.json";
	
	/**
	 * 红树林卡解绑
	 */
	public static final String METHOD_CARDUNBINDING = BASE
			+ ModelEnum.CARD.getCode()
			+ "/card/comm/unbinding.json";
	
	/**
	 * 度假卡使用密码验证
	 */
	public static final String METHOD_CARDCHECKLOGIN = BASEHTTPS
			+ ModelEnum.CARD.getCode()
			+ "/card/idxcard/check_login.json";
	
	/**
	 * 会员红树林卡列表信息
	 */
	public static final String METHOD_CARDARRAY = BASE
			+ ModelEnum.CARD.getCode()
			+ "/card/comm/member_card_array.json";
	
	/**
	 * 某张卡信息查询接口
	 */
	public static final String METHOD_CARDINFO = BASE
			+ ModelEnum.CARD.getCode()
			+ "/card/comm/card_info.json";
	
	/**
	 * 卡消费列表查询（带分页）
	 */
	public static final String METHOD_CONSUMEINFO_ARRAY = BASE
			+ ModelEnum.CARD.getCode()
			+ "/card/comm/consumeinfo_array.json";
	
	/**
	 * 卡账户详细信息查询
	 */
	public static final String METHOD_CARDACCOUNTINFO = BASE
			+ ModelEnum.CARD.getCode()
			+ "/card/comm/card_account_info.json";
	
	/**
	 * 用户绑定卡列表
	 * 2014-8-11上午10:16:33 
	 * @author wangmingyu
	 */
	public static final String CARD_INFO_ARRAY = BASE
	+ ModelEnum.CARD.getCode()
	+"/card/comm/member_card_array.json";
	
	
	
	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-1-15:2010-08-25
	@Description: * 二维码
	**************************************************/
	public static final String QRCODE_SCAN = BASE+ModelEnum.QRCODE.getCode()+"/scan/common/token/querymessage.json";

	/***************************** 红树林卡** 结束 ******************************************************/
	
	/*********************************************************/
	/**
	 * 支付渠道接口
	 * @author wangmingyu
	 * 2014-9-26
	 */
	public static final String PAY_CHANNEL_ARRAY = BASE+ModelEnum.PAY.getCode()+ "/setting/query/payment_channel_array.json";
	
	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-3-24:2010-08-25
	@Description:改版后使用度假宝的 渠道接口
	**************************************************/
	public static final String DJB_PAY_CHANNEL_ARRAY= BASE+ModelEnum.DJB.getCode()+ "/pay/setting/pay_mode_array.json";
	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-1-16:2010-08-25
	@Description:预支付
	**************************************************/
	public static final  String PAYMENT_AMOUNT =BASE+ModelEnum.PAY.getCode()+"/payment/wechat/prepay.json";
	
	
	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-1-19:2010-08-25
	@Description:查询订单信息
	**************************************************/
	public static final  String PAYMENT_ORDER_INFO_URL =BASE+ModelEnum.PAY.getCode()+"/order/query/payorder_info.json";
	
	
	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-1-21:2010-08-25
	@Description:扣款计算（账户）
	**************************************************/
	public static final  String PAYMENT_ACCOUNT_CALCULATE_URL =BASE+ModelEnum.PAY.getCode()+"/payment/account/calculate.json";
	
	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-3-24:2010-08-25
	@Description:描述主要实现的功能（度假宝专用接口----账户扣款计算）
	**************************************************/
	public static final  String DJB_ACCOUNT_CALCULATE_URL =BASE+ModelEnum.DJB.getCode()+"/pay/account/budget.json";
	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-1-21:2010-08-25
	@Description:扣款计算（卡）
	**************************************************/
	public static final  String PAYMENT_CARD_CALCULATE_URL =BASE+ModelEnum.PAY.getCode()+"/payment/epaycard/calculate.json";
	
	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-1-21:2010-08-25
	@Description:卡支付
	**************************************************/
	public static final  String PAYMENT_EPAYCARD_EXEC_PAY_URL=BASE+ModelEnum.PAY.getCode()+"/payment/epaycard/exec_pay.json";
	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-1-21:2010-08-25
	@Description:账户支付
	**************************************************/
	public static final  String PAYMENT_ACCOUNT_PAY_URL=BASE+ModelEnum.PAY.getCode()+"/payment/account/exec_pay.json";
	
	/*************************************************
	Copyright:bupt
	
	@author: 王明雨
	@date:2015-3-24:2010-08-25
	@Description：（度假宝专用接口 --账户扣款支付）
	**************************************************/
	public static final  String DJB_ACCOUNT_PAY_URL=BASE+ModelEnum.DJB.getCode()+"/pay/account/payment.json";

	/*********************************************************/
	/**
	 * 版本更新
	 * 2014-9-26
	 */
	public static final String MEM_VERSIONS = BASE+ModelEnum.UUM.getCode()+ "/common/versions.json";
	
	
	

}

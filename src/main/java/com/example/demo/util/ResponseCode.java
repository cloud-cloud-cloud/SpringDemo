/**
 * @Title:   [ResponseCode.java]
 * @Package: [com.qf.ndes.util]
 * @author:  [ChangcaiCao] 
 * @CreateDate: [2017年3月24日 下午1:25:51]   
 * @UpdateUser: [ChangcaiCao]   
 * @UpdateDate: [2017年3月24日 下午1:25:51]   
 * @UpdateRemark: [说明本次修改内容]
 * @Description:  [TODO(用一句话描述该文件做什么)]
 * @version: [V1.0]
 */
package com.example.demo.util;

/**
 * @ClassName: ResponseCode
 * @author:  [ChangcaiCao] 
 * @CreateDate: [2017年3月24日 下午1:25:51]   
 * @UpdateUser: [ChangcaiCao]   
 * @UpdateDate: [2017年3月24日 下午1:25:51]   
 * @UpdateRemark: [说明本次修改内容]
 * @Description:  [错误码定义]
 * 
 * 1.参数检验的错误分配范围：4001-4099
 * 2.Policy流程的错误分配范围：4101-4199
 * 3.三方服务的错误分配范围：4201-4299
 * 4.Rule服务的错误分配范围：4301-4399
 * 5.Bom服务的错误分配范围：4401-4499
 * 
 * @version: [V1.0]
 */
public class ResponseCode {

	//成功
	public static final int SUCCESS = 2000;
	
	//系统异常
	public static final int SYSTEM_ERROR = 5000;
	
	//未知错误
	public static final int UNKNOW_ERROR = 4000;
	
	//参数为空
	public static final int PARAM_DATA_IS_NULL = 4001;
	
	//Policy编码不存在
	public static final int POLICY_CODE_NOT_EXISTS = 4101;
	
	//actionType不存在
	public static final int ACTION_TYPE_NOT_EXISTS = 4102;
	
	//Policy流程已经存在
	public static final int POLICY_FLOW_EXISTS = 4103;
	
	//Policy流程启动失败
	public static final int POLICY_FLOW_FAIL = 4104;
	
	//三方服务不允许调用
	public static final int NOT_ALLOWED_INVOKE_TRD_SERVICE = 4201;
	
	//三方参数验证失败
	public static final int TRD_PARAM_VERIFY_FAIL = 4202;
	
	//请求三方服务异常
	public static final int THIRD_SERVICE_REQUEST_EXCEPTION = 4203;
	
	
}

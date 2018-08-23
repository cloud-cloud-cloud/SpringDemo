/**
 * @Title:   [CallBack.java]
 * @Package: [com.qf.ndes.entity]
 * @author:  [ChangcaiCao] 
 * @CreateDate: [2017年4月22日 下午4:21:02]   
 * @UpdateUser: [ChangcaiCao]   
 * @UpdateDate: [2017年4月22日 下午4:21:02]   
 * @UpdateRemark: [说明本次修改内容]
 * @Description:  [TODO(用一句话描述该文件做什么)]
 * @version: [V1.0]
 */
package com.example.demo.entity;

import com.example.demo.cache.CacheData;

import java.util.Date;

/**
 * @ClassName: CallBack
 * @author:  [ChangcaiCao] 
 * @CreateDate: [2017年4月22日 下午4:21:02]   
 * @UpdateUser: [ChangcaiCao]   
 * @UpdateDate: [2017年4月22日 下午4:21:02]   
 * @UpdateRemark: [说明本次修改内容]
 * @Description:  [回调配置]
 * @version: [V1.0]
 */
public class CallbackCfg implements CacheData {

	/*
	 * 主键
	 */
	private Long id;
	
	/*
	 * POLICY类型
	 */
	private String policyType;
	
	/*
	 * 回调地址
	 */
	private String invokeUrl;

	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public String getInvokeUrl() {
		return invokeUrl;
	}

	public void setInvokeUrl(String invokeUrl) {
		this.invokeUrl = invokeUrl;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String getDataKey() {
		return policyType;
	}

	@Override
	public long lastUpdateTime() {
		return updateTime.getTime();
	}
}

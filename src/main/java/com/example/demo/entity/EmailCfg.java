package com.example.demo.entity;

import com.example.demo.cache.CacheData;
import freemarker.template.Template;

import java.util.Date;

/**
 * @ClassName: CallBack
 * @author: [ChangcaiCao]
 * @CreateDate: [2017年4月22日 下午4:21:02]   
 * @UpdateUser: [ChangcaiCao]
 * @UpdateDate: [2017年4月22日 下午4:21:02]   
 * @UpdateRemark: [说明本次修改内容]
 * @Description: [回调配置]
 * @version: [V1.0]
 */
public class EmailCfg implements CacheData {

	/*
	 * 主键 - 邮件类型
	 */
	private String emailType;

	/*
	 * 主题
	 */
	private String subject;

	/*
	 * 收件人列表
	 */
	private String receiverList;

	/*
	 * 抄送人列表
	 */
	private String ccList;

	/*
	 * 邮件模板
	 */
	private String template;


	private Template fmTemplate;

	/*
	 * 更新时间
	 */
	private Date updateTime;

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getReceiverList() {
		return receiverList;
	}

	public void setReceiverList(String receiverList) {
		this.receiverList = receiverList;
	}

	public String getCcList() {
		return ccList;
	}

	public void setCcList(String ccList) {
		this.ccList = ccList;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Template getFmTemplate() {
		return fmTemplate;
	}

	public void setFmTemplate(Template fmTemplate) {
		this.fmTemplate = fmTemplate;
	}

	@Override
	public String getDataKey() {
		return emailType;
	}

	@Override
	public long lastUpdateTime() {
		return updateTime.getTime();
	}
}

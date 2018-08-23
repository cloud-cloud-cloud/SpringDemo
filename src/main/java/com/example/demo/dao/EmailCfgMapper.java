/**
 * @Title:   [UserMapper.java]
 * @Package: [com.quark.wreport.mapper]
 * @author:  [ChangcaiCao] 
 * @CreateDate: [2017年2月23日 下午5:46:57]   
 * @UpdateUser: [ChangcaiCao]   
 * @UpdateDate: [2017年2月23日 下午5:46:57]   
 * @UpdateRemark: [说明本次修改内容]
 * @Description:  [TODO(用一句话描述该文件做什么)]
 * @version: [V1.0]
 */
package com.example.demo.dao;

import com.example.demo.entity.EmailCfg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: UserMapper
 * @author:  [ChangcaiCao] 
 * @CreateDate: [2017年2月23日 下午5:46:57]   
 * @UpdateUser: [ChangcaiCao]   
 * @UpdateDate: [2017年2月23日 下午5:46:57]   
 * @UpdateRemark: [说明本次修改内容]
 * @Description:  [TODO(用一句话描述该文件做什么)]
 * @version: [V1.0]
 */

@Mapper
public interface EmailCfgMapper {

	//根据主题查询邮件配置
	EmailCfg selectBySubject(String subject);
	
	/*
	 * 根据邮件类型查询
	 */
	EmailCfg selectByEmailType(String emailType);
	
	/*
	 * 查询所有
	 */
	List<EmailCfg> selectAll();
	
	/*
	 * 新增
	 */
	void insert(EmailCfg emailCfg);
	
	/*
	 * 修改
	 */
	void update(EmailCfg emailCfg);
	
	/*
	 * 根据邮件类型删除
	 */
	void deleteByEmailType(String emailType);

	/**
	 * 根据条件查询邮件配置
	 * @param emailCfg
	 * @return
	 */
	List<EmailCfg> selectByConditon(EmailCfg emailCfg);


	List<EmailCfg> selectByGreaterUpdateTime(EmailCfg emailCfg);

}

package com.example.demo.dao;

import com.example.demo.entity.MsgPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface MsgQueueMapper {

	void insert(MsgPo msgPo);

	int deleteById(long id);

	int deleteAborted(Date nextRetryTime, Date limitTime);

	List<MsgPo> select(Map<String, Object> condition);

	int update(@Param("u") Map<String, Object> update, @Param("c") Map<String, Object> condition);

}

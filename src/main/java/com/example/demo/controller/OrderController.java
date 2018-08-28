package com.example.demo.controller;

import com.example.demo.components.queue.MsgProducer;
import com.example.demo.entity.MsgPo;
import com.example.demo.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by HMa on 2018/8/24.
 */
@Controller
@RequestMapping("order")
public class OrderController {
	@Autowired
	private MsgProducer msgProducer;

	@RequestMapping("/createOrder")
	@ResponseBody
	public ResponseData  createOrder(@RequestBody MsgPo msgPo){

		msgProducer.produce("launch",null,null);//将订单消息放在消息队列
		return null;

	}
}

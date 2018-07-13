package com.example.demo;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by HMa on 2018/6/28.
 */
public class DateDemo {

	public static void main(String[] args) {
		Date now = DateUtils.truncate(new Date(), Calendar.SECOND);//时间精确到秒
		Date minuteNow = DateUtils.truncate(new Date(), Calendar.MINUTE);//精确到分钟
		System.out.println(minuteNow);//
		System.out.println(new Date());
	}
}

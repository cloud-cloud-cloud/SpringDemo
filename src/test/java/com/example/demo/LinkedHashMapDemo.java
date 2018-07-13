package com.example.demo;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by HMa on 2018/6/28.
 */
public class LinkedHashMapDemo {
	private static Map map;
	public static void main(String[] args) {
		map=new LinkedHashMap();//linkedHashMap双向链表 是有顺序的，按照插入的顺序写入和写出；
		map.put("999","bbb");
		map.put("333","ccc");//不允许重复key会被覆盖
		map.put("333","aaa");
        Iterator iterator=map.entrySet().iterator();
		while (iterator.hasNext()){
		Map.Entry entry=(Map.Entry)iterator.next();//map.entry    A map entry (key-value pair)
			Object key=entry.getKey();
			System.out.println("key:"+key);
			Object value=entry.getValue();
			System.out.println("value:"+value);
		}
		int i=0;
		for(;;){
			//执行到满足条件跳出，要不然死循环；
			int a=i++;
			System.out.println("jump to a:"+a);
			if(a==10)
			{
				return;
			}
		}


	}
}

package com.example.demo.link;

/**
 * Created by HMa on 2018/8/1.
 */
public class Node {
	int data;//数据域存储数据
	Node  next;//指针域用于存储下一个节点的地址

	public Node(int data){
		this.data=data;

	}

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}
}

package com.example.demo.link;

import java.util.Stack;

/**
 * Created by HMa on 2018/8/1.
 */
public class Reverse {

	//利用遍历反转单向链表
	public static Node reverseList(Node head){
		if(head==null){
			return null;
		}if(head.getNext()==null){
			return head;
		}

		Node pre=head;
		Node cur=head.getNext();
		while(cur.getNext()!=null){
			Node tmp=cur.getNext();//临时节点 ，用于保存当前当前节点的指针域
			cur.setNext(pre);//反转指针域的执行啊
			pre=cur;//指针往下移动
			cur=tmp;

		}
		cur.setNext(pre);//这个时候当前节点的下一个节点为null的情况，将头节点放到源节点的末尾
		head.setNext(null);//将头节点置为null,作为原链表的尾节点
		return cur;
	}

	/**
	 * 通过遍历反转单向链表
	 * @param head
	 * @return
	 */
	public  static Node reverseByErgodic(Node head){
		if(head==null||head.getNext()==null){
			return head;
		}
		Node reHead=reverseByErgodic(head.getNext());//先反转后续节点head.getNext
		head.getNext().setNext(head);//反转 将当前指针域指向前一个节点
		head.setNext(null);//将源节点头结点置空，成为尾节点
		return reHead;//反转后新链表的头结点
	}

	//利用栈的先进后出的原理---todo 待修改方法
	public static Node listReverseStack(Node node){
		Stack<Node> stack=new Stack<Node>();
		if(node!=null){
			stack.push(node);//放入元素
			node=node.next;
		}
		return stack.peek();//

	}

	public static void main(String[] args) {
		Node head=new Node(0);
		Node node1=new Node(1);
		Node node2=new Node(2);
		Node node3=new Node(3);
		Node h=head;
		head.setNext(node1);
		node1.setNext(node2);
		node2.setNext(node3);
		while(h!=null){
			System.out.println(h.getData());
			h=h.getNext();
		}
		System.out.println("*****************");
		 head= listReverseStack(head);//反转元素
		while(head!=null){
			System.out.println(head.getData()+"  ");
			head=head.getNext();
		}
	}
}

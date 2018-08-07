package com.example.demo;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by HMa on 2018/7/18.
 * 排序的方式有多种 ，归根结底 都是 (x < y) ? -1 : ((x == y) ? 0 : 1)
 */
public class SortTest {

	private static Logger logger= LoggerFactory.getLogger(SortTest.class);
	private static List<Integer> list= Arrays.asList(3,6,7,1,2,0,0);
	public static void main(String[] args) {
//		Collections.sort(list);//第一种排序方式
//		list.sort(Comparator.naturalOrder());
        list.sort(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
//				return o1.compareTo(o2);
				return o1<o2?-1:((o1==o2)?0:1);//==> o1.compareTo(o2)
			}
		});
		List arrayList=new ArrayList<>();
		for(int i=0;i<list.size();i++){
         arrayList.add(list.get(i));
			System.out.println("++++++++"+list.get(i));
		}
		/**转换数组**/
		List<String> list= Arrays.asList("123","345","333333333");
		String[] strArr=(String[]) list.toArray();
		logger.info("str:{}",strArr);
		List list1=new ArrayList<>();
		list1.addAll(list);//将一个集合装载到另外一个集合里面
		logger.info("list1:"+list1);

		//list去重  可以通过hashSet不允许重复元素，不保证添加顺序，LinkedHashSet不允许有重复元素但是 可以保证添加顺序
		//list去重  通过创建一个新的list  通过比对新的数组是否已经放入了这个元素 ，如果已经有了就不放入；另外就是对过比对值如果相等 删除其中一个
        int[] arr={6,12,33,87,90,97,108,561};
        int position=  binarySearch(arr,87,0,arr.length-1);//递归
		if(position==-1){
			System.out.println("未找到该数字");
		}else{
			System.out.println("查找的数字所在的位置："+position);
		}
		String a="888888";
		a.length();



	}

	/**
	 * 按照年龄大小进行排序，如果年龄相同，按照name进行排序
	 */
	@Test
	public void testList(){
//		List list1=new ArrayList<>(10);
		List<Person> list=new ArrayList<Person>();
		list.add(new Person(34,"JSON"));
		list.add(new Person(44,"TOIM"));
		list.add(new Person(17,"NIKE"));
		list.add(new Person(17,"FINANCY"));
		list.add(new Person(45,"HOME"));
		list.sort(new Comparator<Person>() {
			@Override
			public int compare(Person o1, Person o2) {
				if(o1.getAge()!=o2.getAge()){
					return o1.getAge()-o2.getAge();
				}else{
					return o1.getName().compareTo(o2.getName());
				}

			}
		});
		for (int i=0;i<list.size();i++){
			logger.info("age:{},name:{}",list.get(i).getAge(),list.get(i).getName());
		}
	}

	@Test
	public void arrayFixLength(){
		List<String> list= Arrays.asList("123","345","333333333");
	/*	List<String> list1=new ArrayList<>(1);
		list1.add("333333");
		list1.add("31");
		list1.add("12");*/
//		list.add("tewst");//定长不增增加或者删除
//		list.remove("123");
		for(String str:list){
			System.out.println("output:"+str);
		}
	}


	/**对hashmap进行排序**/
	public  void hashMapSort()
	{
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("eee","vvvv");
		map.put("fff","vvvv");
		map.put("aa","vvvv");
	Set<Map.Entry<String,Object>> entry=	map.entrySet();
		List<Map.Entry<String,Object>> list=new ArrayList<Map.Entry<String,Object>>(entry);
		Collections.sort(list, new Comparator<Map.Entry<String, Object>>() {
			@Override
			public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		while(list.iterator().hasNext()){
			String key=list.iterator().next().getKey();
		}


	}


	/**对treeset进行排序**/
	public void setSort()
	{
		Set<Person> set=new TreeSet<Person>(new Comparator<Person>() {
			@Override
			public int compare(Person o1, Person o2) {
				return o1.getAge()-o2.getAge();
			}
		});

		set.add(new Person(18,"333333"));
		set.add(new Person(44,"323"));
		set.add(new Person(12,"22"));
		for(Person student:set){
			logger.info("age:{},name:{}",student.getAge(),student.getName());
		}


	}

	  class  Person {
		private String name;
		private int age;

       Person(int age,String name){
			this.name=name;
			this.age=age;
		}
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}
	/**
	 * 冒泡排序：两个值进行比较，将值大的元素交换到右端。
	 */
	public void bubbleSort(){
		int[] ints={7,1,4,8,9,3,2};
		for(int i=0;i<ints.length-1;i++){//控制排序次数

			for(int j=0;j<ints.length-1-i;j++) {//控制每一趟排序的次数
				if(ints[j]<ints[j+1]){
					int temp=ints[j];
					ints[j]=ints[j+1];
					ints[j+1]=temp;
				}
			}
		}
		for(int x:ints){
			System.out.println("num:"+x);
		}
	}

	/**
	 * 穷举算法
	 *
	 * 一、甲 、乙、丙 三位球迷分别预测进入半决赛的四队A、B、C、D的名次如下：
	 甲：A 第一名 、B 第二名
	 乙：C 第一名 、D 第三名
	 丙：D第一名 、 A 第三名
	 设比赛结果，四队互不相同，并且甲乙丙的预测各对一半，求A、B、C、D队的名次？
	 */
	public void  continueD(){
		int a,b,c,d;
	      boolean flag;
		for(a=1;a<=4;a++){
			for(b=1;b<=4;b++){
				if(a==b){
					continue;
				}
				for(c=1;c<=4;c++){
					if(c==b||c==a){
						continue;
					}
					d=10-a-b-c;
					flag=(a==1)!=(b==2)&&(c==1)!=(d==3)&&(d==1)&&(a==3);
					if(flag){
						System.out.println("A="+a);
						System.out.println("B="+b);
						System.out.println("C="+c);
						System.out.println("D="+d);
					}
				}
			}
		}
	}

	/**
	 * 二分查找算法用递归和循环两种方式
	 */
	public int  binarySearch(int[] arr,int x){
		//循环实现二分查重,已排好序的数组x,需要查找的数-1，
		int  low=0;
		int  high=arr.length-1;
		while(low<=high){
			int middle=(low+high)/2;
			if(x==arr[middle]){
				return middle;
			}else if(x<arr[middle]){
				high=middle-1;
			}else{
				low=high+1;
			}
		}
		return -1;

	}

	/**
	 * 循环实现二分查重
	 * @param dataset
	 * @param data
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	public static  int binarySearch(int[] dataset,int data,int beginIndex,int endIndex){
		int midIndx=(beginIndex+endIndex)/2;
		if(data<dataset[beginIndex]||data>dataset[endIndex]||beginIndex>endIndex){
			return -1;
		}
		if(data<dataset[midIndx]){
			return binarySearch(dataset,data,beginIndex,midIndx-1);
		}else if(data>dataset[midIndx]){
			return  binarySearch(dataset,data,midIndx+1,endIndex);
		}else{
			return midIndx;
		}

	}

	/**
	 * 求一个数组里面有几对相加等于某个数的，例如说2+3=5或者3+2=5这样可以重复
	 * @param arr
	 * @param value
	 * @return
	 */
	public static void search(int[] arr,int value){
		 Arrays.sort(arr);
		int low=0;
		int high=arr.length-1;
		while(low<high){
			if(arr[low]+arr[high]<value){
				 low++;
			}else if(arr[low]+arr[high]>value){
				high--;
			}else{
				System.out.println(arr[low]+","+arr[high]);
				low++;
				high--;
			}

		}

	}





}

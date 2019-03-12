package cn.com.java.thread.container;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 对Collection集合容器的高并发扩展：
 * 		对list：CopyOnWriteArrayList
 * 		对set：
 * 
 * 原理：
 * 	当有线程要往CopyOnWriteArrayList/CopyOnWriteArraySet容器中添加元素时，
 * 		首先会copy出一个跟原来的容器一模一样的容器，然后再往新的容器中添加元素，线程添加元素成功后，将执行原先容器的指针重新指向新的容器
 * 	若没有线程往CopyOnWriteArrayList/CopyOnWriteArraySet容器中添加元素时，
 * 		读取这个容器中的元素是不加锁的，故这个容器适用于：读多写少的并发场景
 * @author Administrator
 *
 */
public class CopyOnWriteDemo {

	public static void main(String[] args) {
		CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
		CopyOnWriteArraySet<String> set = new CopyOnWriteArraySet<>();
		
		list.add("1");
		list.add("2");
		set.add("2");
		set.add("3");
		
		//addIfAbsent若容器中存在这个值，就不添加
		list.addIfAbsent("1");
		//add若容器中存在这个值就不添加
		set.add("2");
		for(String str : list) {
			System.out.println(str);
		}
		for(String str : set) {
			System.out.println(str);
		}
	}
}

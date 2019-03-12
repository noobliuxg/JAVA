package cn.com.java.thread.container;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentMap：线程安全且高并发的map容器
 * 		原理：将一个大的map拆分成16个小段，在每个小段中设置一把锁，这样就可以达到同时16个线程同步访问这个大的map容器的作用，提高并发性能 
 * @author Administrator
 *
 */
public class ConcurrentMapDemo {

	public static void main(String[] args) {
		ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
		map.put("1", 1);
		//putIfAbsent添加元素时，若以前ConcurrentHashMap容器中有这个key-value对，则不添加
		map.putIfAbsent("1", 0);
		for(Entry<String,Integer> entry :map.entrySet()) {
			System.out.println(entry.getKey()+","+entry.getValue());
		}
		
	}
}

package cn.com.java.thread.container;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * 对Queque高并发的扩展：
 * 		ConcurrentLinkedQueue：高效无阻塞的队列
 * 		BlockingQueue：阻塞的队列
 * 			几个主要的实现类：
 * 				ArrayBlockingQueue：基于数组实现的又缓冲的阻塞队列，且必须指明队列的大小，生成和消费不能并发执行
 * 					原理：内部维护一个定长数组，且没有实现读写分离，故不能做到生成和消费并发执行，
 * 				LinkedBlockingQueue：基于链表实现的有缓冲的阻塞队列，且不必指明队列的大小
 * 					原理：内部维护一个链表接口，且实现了读写分离锁，故能做到生成和消费并发执行
 * 				SynchronousQueue：一个没有缓冲的队列，必须是实时地生成和消费
 * 				PriorityBlockingQueue<Object必须实现Compont接口>：基于优先集的阻塞队列，
 * 					原理：内部采用的是一种公平锁，在取出元素时进行排序，而不是在运行时
 * 				DelayQueue<Object必须实现Delayed接口>：一个带有延迟时间的Queque，即延迟时间到了，才能从队列中拿出元素
 * 
 * @author Administrator
 *
 */
public class QueueDemo {

	public static void main(String[] args) throws InterruptedException {
//		//高性能无阻塞无界队列：ConcurrentLinkedQueue
		ConcurrentLinkedQueue<String> q = new ConcurrentLinkedQueue<String>();
		q.offer("a");
		q.offer("b");
		q.offer("c");
		q.offer("d");
		q.add("e");
		
		System.out.println(q.poll());	//a 从头部取出元素，并从队列里删除
		System.out.println(q.size());	//4
		System.out.println(q.peek());	//b
		System.out.println(q.size());	//4
		
		ArrayBlockingQueue<String> array = new ArrayBlockingQueue<String>(5);
		array.put("a");
		array.put("b");
		array.add("c");
		array.add("d");
		array.add("e");
		array.add("f");
		//System.out.println(array.offer("a", 3, TimeUnit.SECONDS));
		
		
		//阻塞队列
		LinkedBlockingQueue<String> q3 = new LinkedBlockingQueue<String>();
		q3.offer("a");
		q3.offer("b");
		q3.offer("c");
		q3.offer("d");
		q3.offer("e");
		q3.add("f");
		System.out.println(q.size());
		
//		for (Iterator iterator = q.iterator(); iterator.hasNext();) {
//			String string = (String) iterator.next();
//			System.out.println(string);
//		}
		
		List<String> list = new ArrayList<String>();
		System.out.println(q3.drainTo(list, 3));
		System.out.println(list.size());
		for (String string : list) {
			System.out.println(string);
		}
		
		
		final SynchronousQueue<String> q2 = new SynchronousQueue<String>();
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println(q2.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t1.start();
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				q2.add("asdasd");
			}
		});
		t2.start();		
	}
}

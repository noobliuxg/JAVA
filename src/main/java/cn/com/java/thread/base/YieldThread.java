package cn.com.java.thread.base;

public class YieldThread {
    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            for (int i=0;i<10;i++){
                try {
                    Thread.sleep(500);
                    System.out.println(Thread.currentThread().getName()+"运行i="+i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i==5){
                    System.out.println("线程礼让");
                    Thread.currentThread().yield();
                }
            }
        },"Thread-1");
        t1.start();
        Thread t2 = new Thread(()->{
            for (int i=0;i<10;i++){
                try {
                    Thread.sleep(500);
                    System.out.println(Thread.currentThread().getName()+"运行i="+i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i==5){
                    System.out.println("线程礼让");
                    Thread.currentThread().yield();
                }
            }
        },"Thread-2");
        t2.start();
    }
}

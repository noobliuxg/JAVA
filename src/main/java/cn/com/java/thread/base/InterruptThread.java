package cn.com.java.thread.base;

public class InterruptThread {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getName()+" run");
                Thread.sleep(10000);
                System.out.println(Thread.currentThread().getName()+"休眠结束");
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName()+"休眠被打断");
                return;
            }
            System.out.println(Thread.currentThread().getName()+"运行结束");
        },"Thread-1");
        t1.start();
        Thread.sleep(2000);
        t1.interrupt();
    }
}

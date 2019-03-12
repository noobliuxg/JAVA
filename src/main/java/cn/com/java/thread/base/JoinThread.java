package cn.com.java.thread.base;


public class JoinThread{

    public static void main(String[] args){
        Thread t1 = new Thread(()-> {
            for (int i=0;i<100;i++){
                System.out.println(Thread.currentThread().getName()+"运行，i="+i);
            }
        }, "Thread-1");
        t1.start();
        for (int j=0;j<100;j++){
            if (j>10){
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName()+"运行，j="+j);
        }
    }
}

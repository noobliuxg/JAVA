package cn.com.java;


public class Test {


    public void Test(){

    }
    public static void main(String[] args){
        int i=3,j;
        while(i>0){
            j=3;
            while(j>0){
                if(j<2) break;
                System.out.println("j+and"+i);
                j--;
            }
            i--;
        }
        ((A)new B()).test();

        String str1="交通银行软件开发中心";
        String str2="交通银行"+"软件开发中心";
        String str3=new String("交通银行软件开发中心");
        boolean b1=(str1==str2);
        boolean b2=(str1==str3);
        System.out.println(b1+","+b2);

    }
}

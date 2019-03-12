package cn.com.java;

public class Something {
    public static void main(String[] args){
        String str1="hello";
        String str2="he"+new String("llo");
        System.out.println((str1==str2)+":"+str1.equals(str2));

    }
    public void addOne(final Other o){
        o.i++;
    }
}

class Other{
    public int i;
}


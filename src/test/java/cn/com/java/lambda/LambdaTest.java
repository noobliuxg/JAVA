package cn.com.java.lambda;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class LambdaTest {

    @Test
    public void testSimple(){
        String[] atp = {"Rafael Nadal", "Novak Djokovic",
                "Stanislas Wawrinka",
                "David Ferrer","Roger Federer",
                "Andy Murray","Tomas Berdych",
                "Juan Martin Del Potro"};
        List<String> players = Arrays.asList(atp);

        players.forEach((player)-> System.out.println(player));

        players.forEach(System.out::println);
    }

    @Test
    public void testThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("AABB");
            }
        }).start();
        System.out.println("lambda表达式");
        new Thread(()-> System.out.println("AAABBB")).start();


        System.out.println("lambda表达式 内部类：");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("运行run()");
            }
        };

        Runnable runnable1 = ()->{
            System.out.println("lanbda run()");
        };

        runnable.run();
        runnable1.run();
    }

    @Test
    public void testSort(){
        String[] players = {"Rafael Nadal", "Novak Djokovic",
                "Stanislas Wawrinka",
                "David Ferrer","Roger Federer",
                "Andy Murray","Tomas Berdych",
                "Juan Martin Del Potro"};

        Arrays.sort(players, String::compareTo);

        for (int i = 0; i < players.length; i++) {
            System.out.println(players[i]);
        }

    }

}

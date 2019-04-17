package com.example.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestApplicationTests {

    @Test
    public void contextLoads() {
    }

    public static void main(String[] args) {
//        ArrayList<String> objects = new ArrayList<>();
//        objects.add("11");
//        objects.add("22");
//        List<Object> collect = objects.stream().map(object -> {
//            return null;
//        }).filter(o -> o!=null).collect(Collectors.toList());
//        System.out.println(collect);
//        斐波那契数列
        Stream.iterate(new int[] {0,1},t->new int[]{t[1],t[0]+t[1]}).limit(20).forEach(t-> System.out.println(t[0]+","+t[1]));
    }
}

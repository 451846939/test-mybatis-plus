package com.example.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestApplicationTests {

    @Test
    public void contextLoads() {
    }

    public static void main(String[] args) {
        ArrayList<String> objects = new ArrayList<>();
        objects.add("11");
        objects.add("22");
        List<Object> collect = objects.stream().map(object -> {
            return null;
        }).filter(o -> o!=null).collect(Collectors.toList());
        System.out.println(collect);
    }
}

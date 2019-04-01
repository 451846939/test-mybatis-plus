package com.example.test;

import com.example.test.test.entity.Users;
import com.example.test.test.entity.UsersDTO;
import com.example.test.test.utils.CopyUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author beiluo
 * Email beiluo@uoko.com
 * @version V1.0
 * @description
 * @date 2019/3/8 11:04
 */
@Slf4j
public class Test {
    public static void main(String[] args) {
        Users users = new Users();
        users.setId(1111);
        users.setUsername("sbbbbbbbb");
//        copyTest1(users);
//        copyTest2(users);
//        copyTest3(users);
//        UsersDTO usersDTO = new UsersDTO();
        copyTest4(users);
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < 5000; i++) {
//            Object o = CopyUtils.copyObjOnAnnotation(users, UsersDTO.class);
//            System.out.println(o);
//        }
//        long end = System.currentTimeMillis();
//        System.out.println(end-start);
    }

    private static void copyTest4(Users users) {
        long start = System.currentTimeMillis();
        Object o = CopyUtils.copyObjOnAnnotation(users, UsersDTO.class);
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        System.out.println(o);
    }

    private static void copyTest3(Users users) {
        Users users1 = new Users();
        users1.setPwd("6545646");
        Object o2 = CopyUtils.copyObj(users, users1,true,false,new String[]{"pwd"},new String[]{"id"});
        System.out.println(o2);
    }

    private static void copyTest2(Users users) {
        Users users1 = new Users();
        users1.setPwd("6545646");
        Object o1 = CopyUtils.copyObjNotNull(users, users1);
        System.out.println(o1);
    }

    private static void copyTest1(Users users) {
        Users users1 = new Users();
        users1.setPwd("6545646");
        Object o = CopyUtils.copyObjNotNull(users, Users.class);
        System.out.println(o);
    }
}

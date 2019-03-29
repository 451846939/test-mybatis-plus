package com.example.test;

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
        try {
            throw new Exception("aaaaa");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("err: ",e);
        }
    }
}

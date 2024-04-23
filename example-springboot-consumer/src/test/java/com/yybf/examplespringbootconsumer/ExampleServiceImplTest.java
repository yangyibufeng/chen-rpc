package com.yybf.examplespringbootconsumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author yangyibufeng
 * @Description
 * @date 2024/4/22-12:21
 */
@SpringBootTest
class ExampleServiceImplTest {
    @Resource
    private ExampleServiceImpl exampleService;

    @Test
    void test1() {
        exampleService.test();
    }
}
package com.atguigu.springcloud.alibaba.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class FlowLimitController {

    @GetMapping("/testA")
    public String testA() {
        return "------testA";
    }

    @GetMapping("/testB")
    public String testB() {
        log.info(Thread.currentThread().getName() +  " \t" + "....testB");
        return "------testB";
    }


    @GetMapping("/testD")
    public String testD()
    {
        /* 测试降级规则RT（平均响应时间） 超时 */
//        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        /* 测试降级规则异常比例和异常数 */
        int a = 10 /0;
        log.info("testD 测试RT");

        return "------testD";
    }

    /**
     * sentinel 热key测试，在sentinel中配置参数索引，设置那个参数为热key,
     *      如果超过设置的阈值，则会报错，这个时候一定要设置blockHandler，否则异常将会打印到前台页面
     * @param p1
     * @param p2
     * @return
     */
    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey",blockHandler = "deal_testHotKey")
    public String testHotKey(@RequestParam(value = "p1",required = false) String p1,
                             @RequestParam(value = "p2",required = false) String p2){

        int a = 10/0;
        return "------testHotKey";
    }

    //兜底方法，如果不配置兜底方法，将会把异常打印到前台界面
    public String deal_testHotKey (String p1, String p2, BlockException exception){
        return "------deal_testHotKey,o(╥﹏╥)o";
    }
}

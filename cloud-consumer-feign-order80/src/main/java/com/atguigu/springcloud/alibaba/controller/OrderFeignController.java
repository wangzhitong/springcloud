package com.atguigu.springcloud.alibaba.controller;

import com.atguigu.springcloud.alibaba.service.PaymentFeignService;
import com.atguigu.springcloud.alibaba.entities.CommonResult;
import com.atguigu.springcloud.alibaba.entities.Payment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class OrderFeignController {

    @Resource
    private PaymentFeignService feignService;

    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable long id){
        return feignService.getPayment(id);
    }

    /**
     * 测试openFeign调用超时，在服务端设置接口延时3秒执行，查看客户端调用的情况。
     *  默认openFeign 客户端只等待一秒钟，如果一秒内服务端没有返回数据，则直接报连接超时的错误，
     *  为了避免这种情况的发生，我们需要在feign的客户端进行超时的配置。
     * @return
     */
    @GetMapping(value = "/consumer/payment/feign/timeout")
    public String paymentFeignTimeout(){
        return feignService.paymentFeignTimeout();
    }


}

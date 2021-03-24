package com.atguigu.springcloud.alibaba.service;

import org.springframework.stereotype.Component;

/**
 * 自定义一个类，实现服务端接口类的所有方法，来处理异常
 *
 * 异常处理类的优先级高于全局配置的异常处理和单独指定的异常处理。
 */
@Component
public class PaymentFallbackService implements PaymentHystrixService {
    @Override
    public String paymentInfo_OK(Integer id) {
        return "-----PaymentFallbackService fall back-paymentInfo_OK , (┬＿┬)";
    }

    @Override
    public String paymentInfo_TimeOut(Integer id) {
        return "-----PaymentFallbackService fall back-paymentInfo_TimeOut , (┬＿┬)";
    }
}

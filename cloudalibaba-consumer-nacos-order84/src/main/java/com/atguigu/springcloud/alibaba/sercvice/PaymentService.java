package com.atguigu.springcloud.alibaba.sercvice;

import com.atguigu.springcloud.alibaba.entities.CommonResult;
import com.atguigu.springcloud.alibaba.entities.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "nacos-payment-provider",fallback = PaymentFallbackService.class)
public interface PaymentService {

    /**
     * 使用openFeign配置接口，只需要保证@GetMapping(value = "/paymentSQL/{id}") 名字与服务接口名一致就可以，
     *  至于方法名称可以不用保持一致。
     * @param id
     * @return
     */
    @GetMapping(value = "/paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id);
}

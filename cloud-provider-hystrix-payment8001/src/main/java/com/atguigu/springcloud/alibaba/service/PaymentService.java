package com.atguigu.springcloud.alibaba.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    //成功
    public String paymentInfo_OK(Integer id){
        return "线程池："+Thread.currentThread().getName()+"   paymentInfo_OK,id：  "+id+"\t"+"哈哈哈"  ;
    }


    /**
     * 调用服务方法失败并抛出了错误信息后，会自动调用@HystrixCommand标注好的fallbackMethod，调用类中指定的方法。
     *
     * 注：使用@HystrixCommand注解后，主要在启动类上增加激活注解@EnableCircuitBreaker
     * @param id
     * @return
     */
    //失败
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler",commandProperties = {
            //3秒钟以内就是正常的业务逻辑,超过3秒就执行fallbackMethod指定的方法,
            //不论是超时还是方法发生异常都会执行fallbackMethod的方法
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
    })
    public String paymentInfo_TimeOut(Integer id){
        int timeNumber = 2;
        try { TimeUnit.SECONDS.sleep(timeNumber); }catch (Exception e) {e.printStackTrace();}
//        int a= 10/0;
        return "线程池："+Thread.currentThread().getName()+"   paymentInfo_TimeOut,id：  "+id+"\t"+"呜呜呜"+" 耗时(秒)"+timeNumber;
    }

    //兜底方法，当调用方法执行失败后会执行该方法
    public String paymentInfo_TimeOutHandler(Integer id){
        return "线程池："+Thread.currentThread().getName()+"   系统繁忙, 请稍候再试  ,id：  "+id+"\t"+"哭了哇呜";
    }

//    =====================================  服务熔断  ===========================================


    /**
     * 服务熔断：
     *      当请求在窗口期内达到10次，并且失败率达到 60%就会开启服务熔断，熔断后首先进行服务降级，
     *      使用指定的fallback来处理异常请求。在一段时间后（默认是5秒），断路器会进入半开状态，会接让其中的一个请求进行转发。
     *      如果成功，断路器会关闭（成功率达到一定次数），若失败，继续开启。
     *
     *  熔断的三种状态
     *      熔断打开
     *          请求不在进行调用当前服务，内部设置时钟一般为MTTR（平均故障处理时间），当打开时长达到所设置的事件进入熔断状态
     *      熔断关闭
     *          熔断关闭不会对服务进行熔断
     *      熔断半开
     *          部分请求根据规则调用当前服务，如果请求成功并且符合规则任务当前服务正常，关闭熔断
     * @param id
     * @return
     */

    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),   //是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"), //请求次数（请求量阈值），默认值20
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"), //时间窗口期（毫秒），默认值10
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"), //失败率达到多少后跳闸，默认值50
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){
        if (id < 0){
            throw new RuntimeException("*****id 不能负数");
        }
        String serialNumber = IdUtil.simpleUUID();

        return Thread.currentThread().getName()+"\t"+"调用成功,流水号："+serialNumber;
    }
    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id){
        return "id 不能负数，请稍候再试,(┬＿┬)/~~     id: " +id;
    }
}

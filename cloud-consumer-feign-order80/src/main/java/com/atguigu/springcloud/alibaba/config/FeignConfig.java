package com.atguigu.springcloud.alibaba.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    /**
     * 配置Feign的日志打印功能
     *
     * feign的四种日期级别：
     *      NONE ：  默认的不显示任何日志
     *      BASIC：  仅记录请求方法、url、响应状态码及执行时间
     *      HEADERS：除了BASIC中定义的信息外，还包含请求和响应头的信息
     *      FULL：   除了HEADERS中定义的信息外，还有请求和响应的正文和元数据信息
     * @return Logger.Level
     */
    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.BASIC;
    }
}

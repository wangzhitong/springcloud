package com.atguigu.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * IRule配置
 *  注意：配置类不能放在启动类同级或子级目录下
 */
@Configuration
public class MySelfRule {

    /**
     * 随机
     * @return IRule
     */
    @Bean
    public IRule myRule()
    {
        return new RandomRule();
    }
}

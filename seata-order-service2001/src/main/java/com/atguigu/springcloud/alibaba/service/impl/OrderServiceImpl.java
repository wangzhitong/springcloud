package com.atguigu.springcloud.alibaba.service.impl;

import com.atguigu.springcloud.alibaba.dao.OrderDao;
import com.atguigu.springcloud.alibaba.domain.Order;
import com.atguigu.springcloud.alibaba.service.AccountService;
import com.atguigu.springcloud.alibaba.service.OrderService;
import com.atguigu.springcloud.alibaba.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private StorageService storageService;

    @Resource
    private AccountService accountService;


    /**
     * 创建订单-> 调用库存服务扣减库存 -> 调用账户服务扣减账户余额 -> 修改订单状态
     * 下订单->减库存 ->减余额 ->该状态
     *
     *  @GlobalTransactional(name = "fsp-create-order",rollbackFor = Exception.class)
     *  name 全局事务名称，只要保证唯一不重复就可以
     *  rollbackFor  指定回滚条件，在什么情况下要回滚。如： Exception.class 只要发生异常就回滚
     *
     * @param order
     */
    @Override
    @GlobalTransactional(name = "fsp-create-order",rollbackFor = Exception.class)  //seata 全局事务
    public void create(Order order) {

        //新建订单
        log.info("----> 开始新建订单");
        orderDao.create(order);

        //扣减库存
        log.info("----> 订单微服务开始调用库存，做扣减Count");
        storageService.decrease(order.getProductId(),order.getCount());
        log.info("----> 订单微服务开始调用库存，做扣减 end");

        //扣减账户
        log.info("----> 订单微服务开始调用账户，做扣减 Money");
        accountService.decrease(order.getUserId(),order.getMoney());
        log.info("----> 订单微服务开始调用账户，做扣减 end");

        //修改订单状态，从零到1,1代表已经完成
        log.info("----> 修改订单状态开始");
        orderDao.update(order.getUserId(),0);
        log.info("----> 修改订单状态结束");

        log.info("----> 下订单结束");
    }

    /**
     *   TC (Transaction Coordinator) - 事务协调者  （seata服务器）
     *   维护全局和分支事务的状态，驱动全局事务提交或回滚。
     *
     *   TM (Transaction Manager) - 事务管理器       （事务的发起方， @GlobalTransactional 标注的方法）
     *   定义全局事务的范围：开始全局事务、提交或回滚全局事务。
     *
     *   RM (Resource Manager) - 资源管理器           （事务的参与方，也就是不同的数据库）
     *   管理分支事务处理的资源，与TC交谈以注册分支事务和报告分支事务的状态，并驱动分支事务提交或回滚。
     * */

}

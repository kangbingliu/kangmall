package cn.kang.mall.task;

import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.kang.mall.common.Const;
import cn.kang.mall.config.RedisShardedPoolUtil;
import cn.kang.mall.config.RedissonManager;
import cn.kang.mall.service.IOrderService;
import cn.kang.mall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderTask {
	
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private RedissonManager redissonManager;
//  @Scheduled(cron="0 0 0 * * ?") //每天0点一次
//  @Scheduled(cron="0 0 23 * * ?") //每天23点一次
//  @Scheduled(cron="0 */1 * * * ?")//每一分钟(每个1分钟的整数倍)
//  @Scheduled(cron="0 0 */6 * * ?")//每6小时(每个6小时的整数倍)
//  @Scheduled(cron="0 0 */1 * * ?")//每一小时(每个1小时的整数倍)
	
	// 关闭容易时 会调用 但如果使用kill  就不会调用
	// 增加关闭 容器的时间
	@PreDestroy
	public void delLock() {
		RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
	}
	
	
	
  /**
   * 没有分布式锁，运行起来来看日志。
   */
  @Scheduled(cron="0 */1 * * * ?")//每1分钟(每个1分钟的整数倍)
  public void closeOrderTaskV1(){
	  // 默认 关闭订单  2个小时  间隔
      int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
      orderService.closeOrder(hour);
	  log.info("定时任务开启");
  }
  
  
  // 存在死锁问题  有可能锁释放不掉
  //@Scheduled(cron="0 */1 * * * ?")//每1分钟(每个1分钟的整数倍)
  public void closeOrderTaskV2() throws InterruptedException {
	  long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout","5000"));//锁5秒有效期
	  //。和时间戳结合起来用。
	  Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()+lockTimeout));
	  if(setnxResult != null && setnxResult.intValue() == 1){
		  //如果返回值是1，代表设置成功，获取锁
		  closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
	  }else{
	        log.info("没有获得分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
	   }
	}
    
    private void closeOrder(String lockName){
    	//expire命令用于给该锁设定一个过期时间，用于防止线程crash，导致锁一直有效，从而导致死锁。
      RedisShardedPoolUtil.expire(lockName,50);//有效期50秒,防死锁
      log.info("获取{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
      int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
      orderService.closeOrder(hour);
      RedisShardedPoolUtil.del(lockName);//释放锁
      log.info("释放{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
      log.info("=============================");
    }
    
    
    /**
     * 防死锁之分布式锁
     * @throws InterruptedException
     */
//    @Scheduled(cron="0 */1 * * * ?")//每1分钟(每个1分钟的整数倍)
    public void closeOrderTaskV3() throws InterruptedException {
        //防死锁分布式锁
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout","50000"));//锁50秒有效期
        //项目由于历史数据关单订单比较多,需要处理,初次用50s时间,后续改成5s即可.同时50s也为了讲课debug的时候时间长而设置。
        //大家可以根据实际情况，如果历史订单都处理完毕，或者在外部进行洗数据ok，这里的lock的时间应该设置小一些，例如1s 2s 3s 4s 5s就足够啦。
        //这个时间如何用呢，看下面。和时间戳结合起来用。
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()+lockTimeout));
        if(setnxResult != null && setnxResult.intValue() == 1){
            //如果返回值是1，代表设置成功，获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else{
            //如果setnxResult==null 或 setnxResult.intValue() ==0 即 != 1的时候
            //未获取到锁，继续判断,判断时间戳,看是否可以重置获取到锁
            String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);

            //如果lockValue不是空,并且当前时间大于锁的有效期,说明之前的lock的时间已超时,执行getset命令.
            if(lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)){
                String getSetResult = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeout));
                //再次用当前时间戳getset，
                //返回给定 key 的旧值。  ->旧值判断，是否可以获取锁
                // 当 key 没有旧值时，即 key 不存在时，返回 nil 。 ->获取锁
                //这里我们set了一个新的value值，获取旧的值。
                if(getSetResult == null || (getSetResult !=null && StringUtils.equals(lockValueStr,getSetResult))){
                    //获取到锁
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }else{
                    log.info("没有获得分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            }else{
                log.info("没有获得分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            }
        }
    }
    
    
    
    /**
     * Redisson分布式锁实现
     * @throws InterruptedException
     */
//    @Scheduled(cron="0 */1 * * * ?")//每1分钟(每个1分钟的整数倍)
    public void closeOrderTaskV4() throws InterruptedException {
        RLock lock = redissonManager.getRedisson().getLock(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        boolean getLock = false;
        try {
        	// 最好waittime设置为0  如果设置为3  则分布式情况下 有可能重复获取到
        	// 因为 业务的执行  如果小于3s  则会 重复获取
        	// 50s 释放锁
            if(getLock = lock.tryLock(0,50, TimeUnit.SECONDS)){//trylock增加锁
                log.info("===获取{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
                int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
                orderService.closeOrder(hour);
            }else{
                log.info("===没有获得分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            }
        }finally {
            if(!getLock){
                return;  // 没有获取到  就不用释放了
            }
            log.info("===释放分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            lock.unlock();  // 主动释放锁
        }
    }

    
    
}
















	


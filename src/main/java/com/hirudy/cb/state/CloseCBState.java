package com.hirudy.cb.state;

import com.hirudy.cb.cb.AbstractCircuitBreaker;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: Rudy Tan
 * Date: 2018/9/21
 *
 * 熔断器-关闭状态
 */
public class CloseCBState implements CBState {

    /**
     * 进入当前状态的初始化时间
     */
    private long stateTime = System.currentTimeMillis();

    /**
     * 关闭状态，失败计数器，以及失败计数器初始化时间
     */
    private AtomicInteger failNum = new AtomicInteger(0);
    private long failNumClearTime = System.currentTimeMillis();

    public String getStateName() {
        // 获取当前状态名称
        return this.getClass().getSimpleName();
    }

    public void checkAndSwitchState(AbstractCircuitBreaker cb) {
        // 阀值判断，如果失败到达阀值，切换状态到打开状态
        long maxFailNum = Long.valueOf(cb.thresholdFailRateForClose.split("/")[0]);
        if (failNum.get() >= maxFailNum){
            cb.setState(new OpenCBState());
        }
    }

    public boolean canPassCheck(AbstractCircuitBreaker cb) {
        // 关闭状态，请求都应该允许通过
        return true;
    }

    public void countFailNum(AbstractCircuitBreaker cb) {
        // 检查计数器是否过期了，否则重新计数
        long period = Long.valueOf(cb.thresholdFailRateForClose.split("/")[1]) * 1000;
        long now = System.currentTimeMillis();
        if (failNumClearTime + period <= now){
            failNum.set(0);
        }
        // 失败计数
        failNum.incrementAndGet();

        // 检查是否切换状态
        checkAndSwitchState(cb);
    }
}

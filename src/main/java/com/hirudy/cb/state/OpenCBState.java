package com.hirudy.cb.state;

import com.hirudy.cb.cb.AbstractCircuitBreaker;

/**
 * User: Rudy Tan
 * Date: 2018/9/21
 *
 * 熔断器-打开状态
 */
public class OpenCBState implements CBState {
    /**
     * 进入当前状态的初始化时间
     */
    private long stateTime = System.currentTimeMillis();

    public String getStateName() {
        // 获取当前状态名称
        return this.getClass().getSimpleName();
    }

    public void checkAndSwitchState(AbstractCircuitBreaker cb) {
        // 打开状态，检查等待时间是否已到，如果到了就切换到半开状态
        long now = System.currentTimeMillis();
        long idleTime = cb.thresholdIdleTimeForOpen * 1000L;
        if (stateTime + idleTime <= now){
            cb.setState(new HalfOpenCBState());
        }
    }

    public boolean canPassCheck(AbstractCircuitBreaker cb) {
        // 检测状态
        checkAndSwitchState(cb);
        return false;
    }

    public void countFailNum(AbstractCircuitBreaker cb) {
        // nothing
    }
}

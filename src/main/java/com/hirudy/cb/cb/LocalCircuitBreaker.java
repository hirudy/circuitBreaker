package com.hirudy.cb.cb;

import com.hirudy.cb.state.CloseCBState;

/**
 * User: Rudy Tan
 * Date: 2018/9/22
 *
 * 本地熔断器(把它当成了工厂了)
 */
public class LocalCircuitBreaker extends AbstractCircuitBreaker {
    public LocalCircuitBreaker(String failRateForClose,
                               int idleTimeForOpen,
                               String passRateForHalfOpen, int failNumForHalfOpen){
        this.thresholdFailRateForClose = failRateForClose;
        this.thresholdIdleTimeForOpen = idleTimeForOpen;
        this.thresholdPassRateForHalfOpen = passRateForHalfOpen;
        this.thresholdFailNumForHalfOpen = failNumForHalfOpen;
    }

    public void reset() {
        this.setState(new CloseCBState());
    }

    public boolean canPassCheck() {
        return getState().canPassCheck(this);
    }

    public void countFailNum() {
        getState().countFailNum(this);
    }
}

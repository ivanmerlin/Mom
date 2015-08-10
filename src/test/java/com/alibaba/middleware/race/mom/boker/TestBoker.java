package com.alibaba.middleware.race.mom.boker;

import com.alibaba.middleware.race.mom.broker.Broker;

/**
 * Created by Administrator on 2015/8/10.
 */
public class TestBoker {
    static Broker broker;

    public static void main(String[] args){
        broker = new Broker();
        broker.start();
    }
}

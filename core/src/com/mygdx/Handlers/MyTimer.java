package com.mygdx.Handlers;

import com.badlogic.gdx.Gdx;
import com.mygdx.Interfaces.Publisher;
import com.mygdx.Interfaces.Subscriber;
import com.mygdx.Tools.Constants;
import com.mygdx.Tools.Constants.*;
import java.util.Iterator;
import java.util.LinkedList;

public class MyTimer implements Publisher {

    private LinkedList<TIMER_MD> timerMDs;
    private LinkedList<Subscriber> subscribers;

    public MyTimer() {
        timerMDs = new LinkedList<>();
        subscribers = new LinkedList<>();
    }
    public void update() {
        if (timerMDs.isEmpty()) return;

        Iterator<TIMER_MD> iterator = timerMDs.iterator();
        while (iterator.hasNext()) {
            TIMER_MD timerMd = iterator.next();
            timerMd.time += Gdx.graphics.getDeltaTime();
            if (timerMd.time >= timerMd.goal) {
                notifySubscriber(timerMd.flag, timerMd.subscriber);
                iterator.remove();
            }
        }
    }

    public void start(float seconds, NFLAG flag, Subscriber subscriber) {
        timerMDs.add(new TIMER_MD(0f, seconds, flag, subscriber));
    }
    
    private class TIMER_MD {
        private float time;
        private float goal;
        private Constants.NFLAG flag;
        private Subscriber subscriber;
        public TIMER_MD(float time, float goal, NFLAG flag, Subscriber subscriber) {
            this.time = time;
            this.goal = goal;
            this.flag = flag;
            this.subscriber = subscriber;
        }
    }


    @Override
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifyAllSubscribers(NFLAG flag) {
        for (Subscriber subscriber : subscribers) {
            subscriber.notify();
        }
    }

    @Override
    public void notifySubscriber(NFLAG flag, Subscriber subscriber) {
        subscriber.notify(flag);
    }
}

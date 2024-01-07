package com.mygdx.Handlers;

import com.badlogic.gdx.Gdx;
import com.mygdx.Interfaces.Publisher;
import com.mygdx.Interfaces.Subscriber;
import com.mygdx.Tools.Constants;
import com.mygdx.Tools.Constants.*;
import java.util.Iterator;
import java.util.LinkedList;

public class MyTimer implements Publisher {

    private final LinkedList<TIMER_MD> timers;
    LinkedList<TIMER_MD> timersToAdd;
    private final LinkedList<Subscriber> subscribers;

    public MyTimer() {
        timers = new LinkedList<>();
        timersToAdd = new LinkedList<>();
        subscribers = new LinkedList<>();
    }
    public void update() {
        if (timers.isEmpty() && timersToAdd.isEmpty()) return;

        if (timers.isEmpty()) {
            timers.addAll(timersToAdd);
            timersToAdd.clear();
        }

        LinkedList<TIMER_MD> timersToRemove = new LinkedList<>();
        Iterator<TIMER_MD> iterator = timers.iterator();

        while (iterator.hasNext()) {
            TIMER_MD timerMd = iterator.next();
            timerMd.time += Gdx.graphics.getDeltaTime();
            if (timerMd.time >= timerMd.goal) {
                notifySubscriber(timerMd.flag, timerMd.subscriber);
                timersToRemove.add(timerMd);
            }
        }
        timers.removeAll(timersToRemove);
        timers.addAll(timersToAdd);
        timersToAdd.clear();
    }

    public void start(float seconds, NFLAG flag, Subscriber subscriber) {
        timersToAdd.add(new TIMER_MD(0f, seconds, flag, subscriber));
    }
    
    private static class TIMER_MD {
        private float time;
        private final float goal;
        private final Constants.NFLAG flag;
        private final Subscriber subscriber;
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

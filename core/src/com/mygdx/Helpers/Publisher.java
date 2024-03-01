package com.mygdx.Helpers;

public interface Publisher {    //Used to notify messages to every class that subscribes to this class
    public void addSubscriber(Subscriber subscriber);                   //Used to add a new subscriber
    public void removeSubscriber(Subscriber subscriber);                //Used to remove a specific subscriber
    public void notifyAllSubscribers(String flag);                      //Used to notify all subscribers
    public void notifySubscriber(String flag, Subscriber subscriber);   //Used to notify a specific subscriber
}

package com.mygdx.Helpers;

public interface Subscriber {   //Used to subscribe to the Publisher class
    public void notify(String flag);    //Once an event takes place, a subscriber gets notified according to a given flag
}

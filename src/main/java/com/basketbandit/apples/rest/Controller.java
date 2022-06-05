package com.basketbandit.apples.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Controller<T> {
    Logger log = LoggerFactory.getLogger(Controller.class);
    T getData();
}

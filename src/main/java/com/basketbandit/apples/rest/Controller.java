package com.basketbandit.apples.rest;

public interface Controller<T> {
    void init();
    T getData();
}

package com.weike.data.listener;

public interface OnReduceListener<T> {
    void onReduce(T t);

    void onAdd(T t);
}

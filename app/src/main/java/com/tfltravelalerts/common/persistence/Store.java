package com.tfltravelalerts.common.persistence;

public interface Store<T> {
    
    public T load();
    
    public void save(T object);
}

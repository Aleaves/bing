package com.app.smartcode.entity;

import androidx.annotation.NonNull;

public class Person implements Cloneable{

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

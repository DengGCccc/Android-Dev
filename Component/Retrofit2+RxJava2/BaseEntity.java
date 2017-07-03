package com.dgc.testretrofitrxjava.web;

import java.io.Serializable;

public class BaseEntity<T> implements Serializable {
    private String rd;
    private String rt;
    private String isIn;
    private String isInhn;
    private T data;

    public String getRd() {
        return rd;
    }

    public void setRd(String rd) {
        this.rd = rd;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getIsIn() {
        return isIn;
    }

    public void setIsIn(String isIn) {
        this.isIn = isIn;
    }

    public String getIsInhn() {
        return isInhn;
    }

    public void setIsInhn(String isInhn) {
        this.isInhn = isInhn;
    }
}

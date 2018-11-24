package com.colorbuzztechgmail.spf;

public class ProgressItem {

    long id=0;
    String TAG;


    public ProgressItem(String tag) {
        super();

    }

    public ProgressItem(String tag,long id) {
        super();
        this.id=id;
        this.TAG=tag;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }
}

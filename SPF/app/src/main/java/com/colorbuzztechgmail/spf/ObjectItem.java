package com.colorbuzztechgmail.spf;

public class ObjectItem extends Object {


    public boolean areContentsTheSame(Object newItem) {

        return this.equals(newItem);
    }



    public boolean areItemsTheSame(Object item) {

        return this.hashCode()==item.hashCode();
    }


}

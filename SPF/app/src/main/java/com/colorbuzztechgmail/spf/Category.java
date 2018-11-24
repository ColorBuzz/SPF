package com.colorbuzztechgmail.spf;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

/**
 * Created by PC01 on 23/12/2017.
 */


public class Category implements SortedListAdapter.ViewModel {

    private String name;
    private Integer id=null;

      public Category() {
        super();
    }
    public Category(String name) {
          this.name=name;

    }
    public Category(String name,Integer id) {
        this.name=name;
        this.id=id;

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



}

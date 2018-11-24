package com.colorbuzztechgmail.spf;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by PC01 on 25/11/2017.
 */

public class FileItem implements SortedListAdapter.ViewModel{

    private File file;
    private String name;
    private String category=null;
    private String parent;
    private String path;
    private String date;
    private Bitmap bmp;
    private int id;
    private boolean check;
    private int count=0;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setFile(String path) {
        int last,last1;
        this.file = new File(path);
        last1=file.getName().lastIndexOf(".spf");

        this.name=file.getName().substring(0,last1);
        this.path=file.getAbsolutePath();
        last=file.getParent().lastIndexOf("/")+1;
        this.parent=file.getParent().substring(last,file.getParent().length());
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy\nHH:mm:ss");
        Date lastModified = new Date(file.lastModified());

        this.date= dateFormat.format(lastModified);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getParent() {
        return parent;
    }

    public String getPath() {
        return path;
    }

    public String getDate() {
        return date;
    }

    public File getFile() {
        return file;
    }


    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }


    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getName() {


        return this.name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

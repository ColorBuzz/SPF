package com.colorbuzztechgmail.spf;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mjdev.libaums.fs.UsbFile;

import java.io.IOException;
import java.nio.ByteBuffer;

public class CustomUsbFile implements UsbFile {




    private UsbFile usbFile;
    String parent=null;
    int id=0;


    public CustomUsbFile(@NonNull UsbFile usbFile) {

        super();


        this.usbFile=usbFile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Nullable
    @Override
    public UsbFile search(@NonNull String path) throws IOException {
        return null;
    }

    @Override
    public boolean isDirectory() {
        return usbFile.isDirectory();
    }

    @Override
    public String getName() {
        return usbFile.getName();
    }

    @Override
    public void setName(String newName) throws IOException {

        usbFile.setName(newName);
    }

    @Override
    public long createdAt() {
        return usbFile.createdAt();
    }

    @Override
    public long lastModified() {
        return usbFile.lastModified();
    }

    @Override
    public long lastAccessed() {
        return usbFile.lastAccessed();
    }

    @Override
    public UsbFile getParent() {
        return usbFile.getParent();
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public String[] list() throws IOException {
        return usbFile.list();
    }

    @Override
    public UsbFile[] listFiles() throws IOException {
        return usbFile.listFiles();
    }

    @Override
    public long getLength() {
        return usbFile.getLength();
    }

    @Override
    public void setLength(long newLength) throws IOException {
        usbFile.setLength(newLength);

    }

    @Override
    public void read(long offset, ByteBuffer destination) throws IOException {
        usbFile.read(offset,destination);

    }

    @Override
    public void write(long offset, ByteBuffer source) throws IOException {

        usbFile.read(offset,source);

    }

    @Override
    public void flush() throws IOException {
        usbFile.flush();

    }

    @Override
    public void close() throws IOException {
        usbFile.close();

    }

    @Override
    public UsbFile createDirectory(String name) throws IOException {
        return usbFile.createDirectory(name);
    }

    @Override
    public UsbFile createFile(String name) throws IOException {
        return usbFile.createFile(  name);
    }

    @Override
    public void moveTo(UsbFile destination) throws IOException {
        usbFile.moveTo(destination);

    }

    @Override
    public void delete() throws IOException {
        usbFile.delete();

    }

    @Override
    public boolean isRoot() {
        return usbFile.isRoot();
    }
}

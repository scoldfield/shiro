package com.cmcc.basic.model;

import java.util.Date;

public class BackupFile implements Comparable<BackupFile>{
    /**
     * ���ݵ��ļ�����
     */
    private String name;
    /**
     * ���ݵ��ļ�ʱ��
     */
    private Date time;
    /**
     * ���ݵ��ļ��ĳߴ�
     */
    private int size;
    /**
     * ���ݵ��ļ�����
     */
    private String filetype;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public String getFiletype() {
        return filetype;
    }
    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }
    public int compareTo(BackupFile o) {
        return o.getTime().compareTo(this.getTime());
    }
    @Override
    public String toString() {
        return "BackupFile [name=" + name + ", time=" + time + ", size=" + size
                + ", filetype=" + filetype + "]";
    }
}
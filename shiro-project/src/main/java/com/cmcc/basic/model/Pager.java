package com.cmcc.basic.model;

import java.util.List;

/**
 * ��ҳ����
 * @author Administrator
 *
 * @param <T>
 */
public class Pager<T> {
    /**
     * ��ҳ�Ĵ�С
     */
    private int size;
    /**
     * ��ҳ����ʼҳ
     */
    private int offset;
    /**
     * �ܼ�¼��
     */
    private long total;
    /**
     * ��ҳ������
     */
    private List<T> datas;
    
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public int getOffset() {
        return offset;
    }
    public void setOffset(int offset) {
        this.offset = offset;
    }
    public long getTotal() {
        return total;
    }
    public void setTotal(long total) {
        this.total = total;
    }
    public List<T> getDatas() {
        return datas;
    }
    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
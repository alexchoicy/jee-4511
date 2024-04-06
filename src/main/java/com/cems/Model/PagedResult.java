package com.cems.Model;

import java.io.Serializable;

public class PagedResult<T> implements Serializable {
    private int total;
    private int page;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;
    private T data;

    public PagedResult() {
    }

    public PagedResult(int total, int page, int pageSize, T data) {
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.data = data;
    }


    public boolean isHasNext() {
        return hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        this.hasNext = total > page * pageSize;
        this.hasPrevious = page > 1;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

package com.example.studentapi.model;

import java.util.List;

public class PageResult {
    private List<Student> records;
    private long total;
    private int page;
    private int pageSize;

    public PageResult() {
    }

    public PageResult(List<Student> records, long total, int page, int pageSize) {
        this.records = records;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
    }

    public List<Student> getRecords() {
        return records;
    }
    public void setRecords(List<Student> records) {
        this.records = records;
    }
    public long getTotal() {
        return total;
    }
    public void setTotal(long total) {
        this.total = total;
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
}




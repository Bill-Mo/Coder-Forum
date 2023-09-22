package com.coder.community.entity;

/*
 * encapsulate pages
 */

public class Page {
    
    // Current page
    private int current = 1;
    // Default post per pages
    private int limit = 10;
    // Total posts
    private int rows;
    private int total;
    // Query path
    private String path;
    
    public int getCurrent() {
        return current;
    }
    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }
    public int getLimit() {
        return limit;
    }
    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {   
            this.limit = limit;
        }
    }
    public int getRows() {
        return rows;
    }
    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    
    public int getOffset() {
        return (current - 1) * limit;
    }
    
    // Get total number of pages
    public int getTotal() {
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    // Start page
    public int getFrom() {
        int from = current - 2;
        return from < 1 ? 1 : from;
    }

    // End page
    public int getTo() {
        int to = current + 2;
        total = getTotal();
        return to > total ? total : to;
    }
}

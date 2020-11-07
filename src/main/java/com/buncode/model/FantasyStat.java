package com.buncode.model;

public class FantasyStat {

    private String desc;
    private int count;
    private int multiplier;
    private int total;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public FantasyStat(String desc, int count, int multiplier, int total) {
        this.desc = desc;
        this.count = count;
        this.multiplier = multiplier;
        this.total = total;
    }
}

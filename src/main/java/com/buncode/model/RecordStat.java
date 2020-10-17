package com.buncode.model;

public class RecordStat  {

    private String desc;
    private String holder;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public RecordStat(String record, String holder) {
        this.desc = record;
        this.holder = holder;
    }

}

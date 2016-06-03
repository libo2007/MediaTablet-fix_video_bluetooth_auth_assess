package com.jiaying.mediatablet.utils;

import java.util.Date;

/**
 * Created by Administrator on 2015/10/6 0006.
 */
public class TimeRecord {
    private static TimeRecord ourInstance = new TimeRecord();

    public static TimeRecord getInstance() {
        return ourInstance;
    }

    private TimeRecord() {
        startDate = new Date();
        endDate = new Date();
    }

    private Date startDate = null;
    private Date endDate = null;

    private long duration;

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public long getDuration() {
        return duration;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}

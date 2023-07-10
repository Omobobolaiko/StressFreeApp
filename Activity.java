package com.example.baads;

import com.google.firebase.Timestamp;

public class Activity {
    String daily;
    Timestamp timestamp;

    public Activity() {
    }

    public String getDaily() {
        return daily;
    }

    public void setDaily(String daily) {
        this.daily = daily;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

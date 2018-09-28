package com.gowbing.kunzhong.model.detail;

import com.gowbing.kunzhong.model.Homework;

/**
 * Created by Administrator on 2018-8-26.
 */

public class HomeworkDetail {
    private int status;
    private String message;
    private Homework results;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Homework getResults() {
        return results;
    }

    public void setResults(Homework results) {
        this.results = results;
    }
}

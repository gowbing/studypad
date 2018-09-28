package com.gowbing.kunzhong.model.list;

import com.gowbing.kunzhong.model.Task;

import java.util.List;

/**
 * Created by Administrator on 2018-8-24.
 */

public class TaskList {
    private int status;
    private String message;
    private List<Task> results;

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

    public List<Task> getResults() {
        return results;
    }

    public void setResults(List<Task> results) {
        this.results = results;
    }
}

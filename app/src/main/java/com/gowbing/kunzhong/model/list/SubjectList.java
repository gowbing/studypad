package com.gowbing.kunzhong.model.list;

import com.gowbing.kunzhong.model.Subject;

import java.util.List;

/**
 * Created by Administrator on 2018-8-25.
 */

public class SubjectList {
    private int status;
    private String message;
    private List<Subject> results;

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

    public List<Subject> getResults() {
        return results;
    }

    public void setResults(List<Subject> results) {
        this.results = results;
    }
}

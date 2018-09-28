package com.gowbing.kunzhong.model.detail;

import com.gowbing.kunzhong.model.Learn;

/**
 * Created by Administrator on 2018-8-28.
 */

public class LearnDetail {
    private int status;
    private String message;
    private Learn results;

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

    public Learn getResults() {
        return results;
    }

    public void setResults(Learn results) {
        this.results = results;
    }
}

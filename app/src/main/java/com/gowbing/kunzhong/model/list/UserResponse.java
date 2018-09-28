package com.gowbing.kunzhong.model.list;

import com.gowbing.kunzhong.model.User;

/**
 * Created by Administrator on 2018-8-25.
 */

public class UserResponse {
    private int status;
    private String message;
    private User results;

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

    public User getResults() {
        return results;
    }

    public void setResults(User results) {
        this.results = results;
    }
}

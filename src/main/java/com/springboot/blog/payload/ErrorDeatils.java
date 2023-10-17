package com.springboot.blog.payload;

import java.util.Date;

public class ErrorDeatils {

    private Date timestamp;
    private String message;
    private String details;

    public ErrorDeatils(Date timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}

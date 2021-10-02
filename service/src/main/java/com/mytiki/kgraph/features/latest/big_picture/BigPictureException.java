package com.mytiki.kgraph.features.latest.big_picture;

import org.springframework.http.HttpStatus;

public class BigPictureException extends Exception{
    private final String domain;
    private final HttpStatus status;

    public BigPictureException(String domain, HttpStatus status) {
        super(domain + "-" + status.value());
        this.domain = domain;
        this.status = status;
    }

    public BigPictureException(String domain, Exception e) {
        super(e.getMessage(), e.getCause());
        this.domain = domain;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public String getDomain() {
        return domain;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

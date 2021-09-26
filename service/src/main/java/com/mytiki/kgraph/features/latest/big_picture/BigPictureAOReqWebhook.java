package com.mytiki.kgraph.features.latest.big_picture;

public class BigPictureAOReqWebhook {
    private String id;
    private Integer status; //200 or 404
    private String type;
    private BigPictureAO body;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigPictureAO getBody() {
        return body;
    }

    public void setBody(BigPictureAO body) {
        this.body = body;
    }
}

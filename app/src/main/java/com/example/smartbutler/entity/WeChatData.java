package com.example.smartbutler.entity;
/*
*微信精选的额数据类
*
*
*  */
public class WeChatData {
    //标题，出处，图片的url
    private String title;
    private String source;
    private String imgUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

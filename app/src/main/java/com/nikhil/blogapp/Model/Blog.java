package com.nikhil.blogapp.Model;

public class Blog {
    public String title;
    public String desc;
    public String userid;
    public String timestamp;
    public String image;
//    public String dpimgurl;
//    public String first;

    public Blog() {
    }

    public Blog(String title, String desc, String userid, String timestamp, String image) {
        this.title = title;
        this.desc = desc;
        this.userid = userid;
        this.timestamp = timestamp;
        this.image = image;
//        this.dpimgurl=dpimgurl;
//        this.first=first;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

//    public String getDpimgurl() {
//        return dpimgurl;
//    }
//
//    public void setDpimgurl(String dpimgurl) {
//        this.dpimgurl = dpimgurl;
//    }
//
//    public String getFirst() {
//        return first;
//    }
//
//    public void setFirst(String first) {
//        this.first = first;
//    }
}

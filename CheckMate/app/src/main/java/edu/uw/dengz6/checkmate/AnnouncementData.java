package edu.uw.dengz6.checkmate;

public class AnnouncementData {
    public String content;
    public String createdBy;
    public String createdOn;

    public AnnouncementData(String content, String createdOn, String createdBy) {
        this.content = content;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
    }
}
package edu.uw.dengz6.checkmate;

public class AnnouncementData {
    public String announcementID;
    public String content;
    public String description;
    public String createdOn;
    public String createdBy;

    public AnnouncementData() {
        // default constructor
    }

    public AnnouncementData(String announcementID, String content, String description, String createdOn, String createdBy) {
        this.announcementID =  announcementID;
        this.content = content;
        this.description = description;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
    }
}
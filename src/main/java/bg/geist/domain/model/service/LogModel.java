package bg.geist.domain.model.service;

import java.util.Date;

public class LogModel {
    private Long id;
    private String user;
    private String level;
    private String tags;
    private String action;
    private Date timeStamp;

    public LogModel(Long id, String user, String level, String tags, String action, Date timeStamp) {
        this.id = id;
        this.user = user;
        this.level = level;
        this.tags = tags;
        this.action = action;
        this.timeStamp = timeStamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}

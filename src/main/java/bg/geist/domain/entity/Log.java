package bg.geist.domain.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "logs")
public class Log extends BaseEntity{

    public enum Level{
        TRACE, DEBUG, INFO, WARN, ERROR, FATAL, SECURITY
    }

    public enum Tag{
        USERS, CONTROLLERS, EXERCISES, CARDS, QUIZZES
    }

    @Column(nullable = false, updatable = false)
    private String username;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Level level;

    @Column(nullable = false, updatable = false)
    private String tags;

    @Column(nullable = false, updatable = false)
    private String action;

    @Column(name = "time_stamp", nullable = false, updatable = false)
    @CreationTimestamp
    private Date timeStamp;

    public Log() {};
    public Log(String username, Level level, String tags, String action) {
        this.username = username;
        this.level = level;
        this.tags = tags;
        this.action = action;
    }

    public String getUsername() {
        return username;
    }
    public Level getLevel() {
        return level;
    }
    public String getTags() {
        return tags;
    }
    public String getAction() {
        return action;
    }
    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setLevel(Level level) {
        this.level = level;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
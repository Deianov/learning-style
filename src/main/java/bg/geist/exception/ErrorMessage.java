package bg.geist.exception;

import java.time.Instant;

public class ErrorMessage {
    private int status;
    private String message;
    private String description;
    private final Instant timestamp;


    public ErrorMessage(int status, String message, String description) {
        this.status = status;
        this.message = message;
        this.description = description;
        this.timestamp = Instant.now();
    }

    public int getStatus() {
        return status;
    }

    public ErrorMessage setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorMessage setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ErrorMessage setDescription(String description) {
        this.description = description;
        return this;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    private void setTimestamp(Instant timestamp) {}
}

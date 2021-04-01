package bg.geist.exception;

import java.time.Instant;

public class ErrorMessage {
    private final int status;
    private final String message;
    private final String description;
    private final Long timestamp;


    public ErrorMessage(int status, String message, String description) {
        this.status = status;
        this.message = message;
        this.description = description;
        this.timestamp = Instant.now().toEpochMilli();
    }

    public int getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
    public String getDescription() {
        return description;
    }
    public Long getTimestamp() {
        return timestamp;
    }
}

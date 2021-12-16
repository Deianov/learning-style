package bg.geist.web.api.model;

import org.springframework.http.HttpStatus;

public class ResponseModel {
    private static final int DEFAULT_STATUS = 200;
    private final int status;
    private final String message;
    private final String[][] data;

    public ResponseModel(String message) {
        this.message = message;
        this.status = DEFAULT_STATUS;
        this.data = null;
    }
    public ResponseModel(String message, HttpStatus status) {
        this.status = status.value();
        this.message = message;
        this.data = null;
    }

    public ResponseModel(String message, HttpStatus status, String[][] data) {
        this.status = status.value();
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public String[][] getData() { return data; }
}

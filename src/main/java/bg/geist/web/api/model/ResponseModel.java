package bg.geist.web.api.model;

public class ResponseModel {
    private static final int DEFAULT_STATUS = 200;
    private final int status;
    private final String message;

    public ResponseModel(String message) {
        this.message = message;
        this.status = DEFAULT_STATUS;
    }
    public ResponseModel(String message, int status) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}

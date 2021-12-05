package bg.geist.web.api.model;

import javax.validation.constraints.NotNull;

public class RequestModel {
    @NotNull
    private String username;
    @NotNull
    private String[][] data;


    public String getUsername() {
        return username;
    }

    public String[][] getData() {
        return data;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setData(String[][] data) {
        this.data = data;
    }
}

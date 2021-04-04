package bg.geist.domain.model.view;

public class SimpleProfileView {
    private final String username;
    private final String fullname;
    private final String imageUrl;


    public SimpleProfileView(String username, String fullname, String imageUrl) {
        this.username = username;
        this.fullname = fullname;
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }
    public String getFullname() {
        return fullname;
    }
    public String getImageUrl() {
        return imageUrl;
    }
}
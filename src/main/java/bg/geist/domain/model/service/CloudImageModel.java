package bg.geist.domain.model.service;

public class CloudImageModel {
    private final String publicId; // TODO: 02.04.2021 exists by publicId
    private final String url;


    public CloudImageModel(String publicId, String url) {
        this.publicId = publicId;
        this.url = url;
    }

    public String getPublicId() {
        return publicId;
    }
    public String getUrl() {
        return url;
    }
}
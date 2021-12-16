package bg.geist.domain.model.service;

public class ExerciseModel {
    private Long id;
    private String path;
    private String name;
    private String category;
    private String description;
    private String source;
    private String sourceUrl;
    private String author;
    private String authorUrl;
    private String createdBy;

    public ExerciseModel() { }
    public ExerciseModel(Long id, String path, String name, String category, String description, String source, String sourceUrl, String author, String authorUrl, String createdBy) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.category = category;
        this.description = description;
        this.source = source;
        this.sourceUrl = sourceUrl;
        this.author = author;
        this.authorUrl = authorUrl;
        this.createdBy = createdBy;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
package bg.geist.domain.model.service;

import java.util.Collection;
import java.util.HashMap;

public class MapModel implements ExerciseModel{
    private String name;
    private String category;
    private String description;
    private String source;
    private String sourceUrl;
    private String author;
    private String authorUrl;
    private HashMap<String, Integer> options;

    public MapModel() { }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String getSourceUrl() {
        return sourceUrl;
    }

    @Override
    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String getAuthorUrl() {
        return authorUrl;
    }

    @Override
    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    @Override
    public void setOptions(HashMap<String, Integer> options) {

    }

    @Override
    public void setCorrect(Collection<Integer> correct) {

    }

    @Override
    public HashMap<String, Integer> getOptions() {
        return options;
    }
}

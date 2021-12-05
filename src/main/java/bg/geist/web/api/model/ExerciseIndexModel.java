package bg.geist.web.api.model;

import java.util.ArrayList;
import java.util.Collection;


public class ExerciseIndexModel {

    private String category;
    private Collection<ExerciseLinkModel> links = new ArrayList<>();


    public void addLink(Long id, String text) {
        this.links.add(new ExerciseLinkModel(id, text));
    }

    public ExerciseIndexModel(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Collection<ExerciseLinkModel> getLinks() {
        return links;
    }

    public void setLinks(Collection<ExerciseLinkModel> links) {
        this.links = links;
    }
}
package bg.geist.domain.model.service;

import java.util.Collection;
import java.util.HashMap;

public interface ExerciseModel {
    String getName();
    String getCategory();
    String getDescription();
    String getSource();
    String getSourceUrl();
    String getAuthor();
    String getAuthorUrl();
    HashMap<String, Integer> getOptions();
//    Collection<Integer> getCorrect();

    void setName(String name);
    void setCategory(String category);
    void setDescription(String description);
    void setSource(String source);
    void setSourceUrl(String sourceUrl);
    void setAuthor(String author);
    void setAuthorUrl(String authorUrl);
    void setOptions(HashMap<String, Integer> options);
    void setCorrect(Collection<Integer> correct);
}
package bg.geist.init.dto;

import java.util.Collection;
import java.util.HashMap;


public class QuizDto {
    public String name;
    public String category;
    public String description;
    public String source;
    public String sourceUrl;
    public String author;
    public String authorUrl;
    public HashMap<String, Integer> options;

    public Collection<Integer> correct;
    public Collection<QuestionDto> questions;
}
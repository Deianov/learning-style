package bg.geist.init.dto;

import java.util.Collection;
import java.util.HashMap;


public class QuizDto {
    public HashMap<String, String> exercise;
    public HashMap<String, String> props;
    public Collection<Integer> correct;
    public Collection<QuestionDto> questions;
}
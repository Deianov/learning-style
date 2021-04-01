package bg.geist.init.dto;

import java.util.Collection;

public class QuestionDto {
    public String text;
    public int value;
    public int correct;
    public String type;
    public Collection<AnswerDto> answers;
}

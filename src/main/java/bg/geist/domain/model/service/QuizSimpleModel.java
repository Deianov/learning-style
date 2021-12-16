package bg.geist.domain.model.service;

import java.util.Collection;

public class QuizSimpleModel extends ExerciseParentModel implements QuizModelInt{
    private Collection<Integer> correct;
    private Collection<QuestionSimpleModel> questions;


    public QuizSimpleModel() { }


    public Collection<Integer> getCorrect() {
        return correct;
    }
    public void setCorrect(Collection<Integer> correct) {
        this.correct = correct;
    }
    public Collection<QuestionSimpleModel> getQuestions() {
        return questions;
    }
    public void setQuestions(Collection<QuestionSimpleModel> questions) {
        this.questions = questions;
    }
}
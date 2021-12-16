package bg.geist.domain.model.service;


import java.util.Collection;

public class QuizModel extends ExerciseParentModel implements QuizModelInt{
    private Collection<Integer> correct;
    private Collection<QuestionModel> questions;


    public Collection<Integer> getCorrect() {
        return correct;
    }
    public void setCorrect(Collection<Integer> correct) {
        this.correct = correct;
    }
    public Collection<QuestionModel> getQuestions() {
        return questions;
    }
    public void setQuestions(Collection<QuestionModel> questions) {
        this.questions = questions;
    }
}
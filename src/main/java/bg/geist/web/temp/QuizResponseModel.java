package bg.geist.web.api.quiz.models;


import java.util.Collection;

public class QuizResponseModel {
    private String name;
    private String description;
    private String source;
    private String sourceUrl;
    private String author;
    private String authorUrl;
    private String timeStamp;
    private String updated;
    private Collection<Integer> correct;
    private Collection<QuestionResponseModel> questions;


    public String getName() {
        return name;
    }

    public QuizResponseModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public QuizResponseModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getSource() {
        return source;
    }

    public QuizResponseModel setSource(String source) {
        this.source = source;
        return this;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public QuizResponseModel setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public QuizResponseModel setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public QuizResponseModel setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
        return this;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUpdated() {
        return updated;
    }

    public QuizResponseModel setUpdated(String updated) {
        this.updated = updated;
        return this;
    }

    public Collection<Integer> getCorrect() {
        return correct;
    }

    public QuizResponseModel setCorrect(Collection<Integer> correct) {
        this.correct = correct;
        return this;
    }

    public Collection<QuestionResponseModel> getQuestions() {
        return questions;
    }

    public QuizResponseModel setQuestions(Collection<QuestionResponseModel> questions) {
        this.questions = questions;
        return this;
    }
}
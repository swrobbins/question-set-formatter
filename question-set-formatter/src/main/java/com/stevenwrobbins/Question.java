package com.stevenwrobbins;

import java.util.List;

public class Question {
    private String question;
    private boolean showQuestionPrompt;
    private List<Question> subQuestions;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isShowQuestionPrompt() {
        return showQuestionPrompt;
    }

    public void setShowQuestionPrompt(boolean showQuestionPrompt) {
        this.showQuestionPrompt = showQuestionPrompt;
    }

    public List<Question> getSubQuestions() {
        return subQuestions;
    }

    public void setSubQuestions(List<Question> subQuestions) {
        this.subQuestions = subQuestions;
    }
}

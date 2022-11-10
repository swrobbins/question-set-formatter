package com.stevenwrobbins;

import java.util.List;

public class QuestionSet {
    private String symptomName;
    private String symptomTypeCategoryName;
    private List<Question> questions;

    public String getSymptomName() {
        return symptomName;
    }

    public void setSymptomName(String symptomName) {
        this.symptomName = symptomName;
    }

    public String getSymptomTypeCategoryName() {
        return symptomTypeCategoryName;
    }

    public void setSymptomTypeCategoryName(String symptomTypeCategoryName) {
        this.symptomTypeCategoryName = symptomTypeCategoryName;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}

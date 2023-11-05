package com.example.trip;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WaitingQuizQuestion {
    private String question;
    private String answer;
    private String[] options;
    private boolean askedBefore;

    public WaitingQuizQuestion(String question, String[] options, String answer) {
        this.answer = answer;
        this.question = question;
        this.options = options;
        this.askedBefore = false;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public boolean IsAskedBefore() {
        return this.askedBefore;
    }


    public String[] getMixedOptions() {
        List<String> optionsList = Arrays.asList(options);  // Convert the array to a list

        Collections.shuffle(optionsList);  // Shuffle the list

        return optionsList.toArray(new String[0]);  // Convert the shuffled list back to an array
    }

    public void setAskedBefore(boolean askedBefore) {
        this.askedBefore = askedBefore;
    }

}
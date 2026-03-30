package com.ai.Email.Template.Generator.model;

public class EmailRequest {

    private String prompt;
    private String tone;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }
}
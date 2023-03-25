package model;

import java.util.List;

public class Question {
    private int id;
    private Topic topic;
    private int difficultyRank;
    private String content;
    private List<Response> responses;

    public Question(Topic topic, int difficultyRank, String content, List<Response> responses) {
        this.topic = topic;
        this.difficultyRank = difficultyRank;
        this.content = content;
        this.responses = responses;
    }
    public Question(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public int getDifficultyRank() {
        return difficultyRank;
    }

    public void setDifficultyRank(int difficultyRank) {
        this.difficultyRank = difficultyRank;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", topic=" + topic +
                ", difficultyRank=" + difficultyRank +
                ", content='" + content + '\'' +
                ", responses=" + responses +
                '}';
    }
}

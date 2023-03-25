package model;

public class Response {
    private int id;
    private String text;
    private boolean isCorrect;

    public Response(String text, boolean isCorrect) {
        this.text = text;
        this.isCorrect = isCorrect;
    }
    public Response() {
    }


    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    @Override
    public String toString() {
        return "Response{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}

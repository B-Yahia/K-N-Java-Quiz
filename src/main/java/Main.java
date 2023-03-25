import doa.DoaQuestion;
import model.Question;
import model.Response;
import model.Topic;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        DoaQuestion doaQuestion = new DoaQuestion();


        Topic topic = new Topic("Sample Topic");
        List<Response> responses = new ArrayList<>();
        responses.add(new Response("Response 1", true));
        responses.add(new Response("Response 2", false));
        Question question = new Question(topic,1 , "Sample question content?", responses);

        doaQuestion.saveQuestion(question);
        System.out.println(question.toString());

        Topic updatedTopic = new Topic("Updated Topic");
        updatedTopic.setId(question.getTopic().getId());
        List<Response> updatedResponses = new ArrayList<>();
        Response response1 = new Response("updated Response 1", true);
        response1.setId(responses.get(0).getId());
        updatedResponses.add(response1);
        Response response2 = new Response("updated Response 2", false);
        response2.setId(responses.get(1).getId());
        updatedResponses.add(response2);
        Question updatedQuestion = new Question(updatedTopic,1 , "updated question content?", updatedResponses);
        updatedQuestion.setId(question.getId());
        doaQuestion.updateQuestion(updatedQuestion);
        System.out.println(updatedQuestion.toString());


    }
}

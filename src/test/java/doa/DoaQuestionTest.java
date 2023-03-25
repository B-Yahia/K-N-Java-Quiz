package doa;

import model.Question;
import model.Response;
import model.Topic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class DoaQuestionTest {
    private DoaQuestion doaQuestion;

    @BeforeEach
    void setUp() {
        doaQuestion = new DoaQuestion();
    }

    @AfterEach
    void tearDown() {
        doaQuestion = null;
    }

    @Test
    void testSaveAndGetQuestionById() {
        // Create a question
        Topic topic = new Topic("Sample Topic");
        List<Response> responses = new ArrayList<>();
        responses.add(new Response("Response 1", true));
        responses.add(new Response("Response 2", false));
        Question question = new Question(topic,1 , "Sample question content?", responses);

        // Save the question
        Question savedQuestion = doaQuestion.saveQuestion(question);

        // Search for the question using its ID
        Question foundQuestion = doaQuestion.getQuestionById(savedQuestion.getId());

        // Assert the question is found and its properties match the original question
        assertNotNull(foundQuestion);
        assertEquals(savedQuestion.getId(), foundQuestion.getId());
        assertEquals(savedQuestion.getContent(), foundQuestion.getContent());
        assertEquals(savedQuestion.getDifficultyRank(), foundQuestion.getDifficultyRank());
        assertEquals(savedQuestion.getTopic().getId(), foundQuestion.getTopic().getId());
        assertEquals(savedQuestion.getTopic().getName(), foundQuestion.getTopic().getName());

        // Assert the responses match the original question
        assertNotNull(foundQuestion.getResponses());
        assertEquals(savedQuestion.getResponses().size(), foundQuestion.getResponses().size());

        for (int i = 0; i < savedQuestion.getResponses().size(); i++) {
            assertEquals(savedQuestion.getResponses().get(i).getText(), foundQuestion.getResponses().get(i).getText());
            assertEquals(savedQuestion.getResponses().get(i).isCorrect(), foundQuestion.getResponses().get(i).isCorrect());
        }

        // Clean up the test data
        doaQuestion.deleteQuestion(savedQuestion.getId());
    }

    @Test
    void testUpdateQuestion() {
        // Create a question
        Topic topic = new Topic("Original Topic");
        List<Response> responses = new ArrayList<>();
        responses.add(new Response("Original Response 1", true));
        responses.add(new Response("Original Response 2", false));
        Question question = new Question(topic, 1, "Original question content?", responses);

        // Save the question
        Question saveQuestion = doaQuestion.saveQuestion(question);


        // Update the question properties
        Question newQuestion = new Question();
        newQuestion.setId(saveQuestion.getId());
        newQuestion.setContent("Updated question content?");
        newQuestion.setDifficultyRank(2);
        newQuestion.setTopic(new Topic());
        newQuestion.getTopic().setId(saveQuestion.getTopic().getId());
        newQuestion.getTopic().setName("Updated Topic");
        List<Response> newResponses = new ArrayList<>();
        newResponses.add(new Response("Updated Response 1", true));
        newResponses.add(new Response("Updated Response 2", false));
        newQuestion.setResponses(newResponses);
        newQuestion.getResponses().get(0).setId(saveQuestion.getResponses().get(0).getId());
        newQuestion.getResponses().get(1).setId(saveQuestion.getResponses().get(1).getId());



        // Update the question in the database
        doaQuestion.updateQuestion(newQuestion);

        // Search for the updated question using its ID
        Question updatedQuestion = doaQuestion.getQuestionById(newQuestion.getId());

        // Assert the updated question is different from the original question
        assertNotNull(updatedQuestion);
        assertEquals(saveQuestion.getId(), updatedQuestion.getId());
        System.out.println(saveQuestion.getContent()+ updatedQuestion.getContent());
        assertNotEquals(saveQuestion.getContent(), updatedQuestion.getContent());
        assertNotEquals(saveQuestion.getDifficultyRank(), updatedQuestion.getDifficultyRank());
        assertNotEquals(saveQuestion.getTopic().getName(), updatedQuestion.getTopic().getName());

        // Assert the responses are different from the original question
        assertNotNull(updatedQuestion.getResponses());
        assertEquals(saveQuestion.getResponses().size(), updatedQuestion.getResponses().size());

        for (int i = 0; i < saveQuestion.getResponses().size(); i++) {

            assertNotEquals(saveQuestion.getResponses().get(i).getText(), updatedQuestion.getResponses().get(i).getText());
            assertEquals(saveQuestion.getResponses().get(i).isCorrect(), updatedQuestion.getResponses().get(i).isCorrect());
        }

        // Clean up the test data
        doaQuestion.deleteQuestion(newQuestion.getId());
    }

    @Test
    public void testSearchQuestionByTopic() {
        // Create two different topics
        Topic topic1 = new Topic("Topic 1");
        Topic topic2 = new Topic("Topic 2");

        List<Response> responses = new ArrayList<>();
        responses.add(new Response("Original Response 1", true));
        responses.add(new Response("Original Response 2", false));
        Question question1 = new Question(topic1, 1, "Original question1 content?", responses);
        Question question2 = new Question(topic2, 1, "Original question2 content?", responses);

        // Save the questions
        Question savedQuestion1 = doaQuestion.saveQuestion(question1);
        Question savedQuestion2 = doaQuestion.saveQuestion(question2);

        //Search questions by topic name
        List<Question> questionsWithTopic1List = doaQuestion.searchQuestionByTopic(topic1.getName());
        List<Question> questionsWithTopic2List = doaQuestion.searchQuestionByTopic(topic2.getName());

        //Assert that the search result is matching the topic
        assertNotNull(questionsWithTopic1List);
        assertNotNull(questionsWithTopic2List);
        assertEquals(questionsWithTopic1List.get(0).getTopic().getId(),question1.getTopic().getId());
        assertEquals(questionsWithTopic2List.get(0).getTopic().getId(),question2.getTopic().getId());

        // Clean up the test data
        doaQuestion.deleteQuestion(savedQuestion1.getId());
        doaQuestion.deleteQuestion(savedQuestion2.getId());
    }
}


package doa;

import model.Question;
import model.Response;
import model.Topic;
import repository.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoaQuestion {

    public final DatabaseManager dbManager = DatabaseManager.getInstance();

    public Question saveQuestion(Question question) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;

        try {
            connection = dbManager.getConnection();

            // Insert topic into the topics table if not exists
            String insertTopicSQL = "INSERT INTO topics (name) VALUES (?) ON DUPLICATE KEY UPDATE id=LAST_INSERT_ID(id)";
            preparedStatement = connection.prepareStatement(insertTopicSQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, question.getTopic().getName());
            preparedStatement.executeUpdate();

            // Get the generated topic ID
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int topicId = generatedKeys.getInt(1);
                question.getTopic().setId(topicId);
            }

            preparedStatement.close();
            generatedKeys.close();

            // Insert question into the questions table
            String insertQuestionSQL = "INSERT INTO questions (topic_id, content, difficulty_rank) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertQuestionSQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, question.getTopic().getId());
            preparedStatement.setString(2, question.getContent());
            preparedStatement.setInt(3, question.getDifficultyRank());
            preparedStatement.executeUpdate();

            // Get the generated question ID
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int questionId = generatedKeys.getInt(1);
                question.setId(questionId);

                // Insert the related responses into the responses table
                for (Response response : question.getResponses()) {
                    preparedStatement.close();

                    String insertResponseSQL = "INSERT INTO responses (question_id, text, is_correct) VALUES (?, ?, ?)";
                    preparedStatement = connection.prepareStatement(insertResponseSQL, Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setInt(1, questionId);
                    preparedStatement.setString(2, response.getText());
                    preparedStatement.setBoolean(3, response.isCorrect());
                    preparedStatement.executeUpdate();
                    // Get the generated Response ID
                    generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int responseId = generatedKeys.getInt(1);
                        response.setId(responseId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.closeConnections(connection, preparedStatement, generatedKeys);
        }
        return question;
    }


    public Question updateQuestion(Question question) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbManager.getConnection();

            // Update the topic
            String updateTopicSQL = "UPDATE topics SET name = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(updateTopicSQL);
            preparedStatement.setString(1, question.getTopic().getName());
            preparedStatement.setInt(2, question.getTopic().getId());
            preparedStatement.executeUpdate();

            preparedStatement.close();

            // Update the question
            String updateQuestionSQL = "UPDATE questions SET content = ?, difficulty_rank = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(updateQuestionSQL);
            preparedStatement.setString(1, question.getContent());
            preparedStatement.setInt(2, question.getDifficultyRank());
            preparedStatement.setInt(3, question.getId());
            preparedStatement.executeUpdate();

            preparedStatement.close();

            // Update the responses
            for (Response response : question.getResponses()) {
                String updateResponseSQL = "UPDATE responses SET text = ?, is_correct = ? WHERE id = ?";
                preparedStatement = connection.prepareStatement(updateResponseSQL);
                preparedStatement.setString(1, response.getText());
                preparedStatement.setBoolean(2, response.isCorrect());
                preparedStatement.setInt(3, response.getId());
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.closeConnections(connection, preparedStatement, null);
        }

        return question;
    }


    public void deleteQuestion(int questionId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbManager.getConnection();

            // Delete the associated responses
            String deleteResponsesSQL = "DELETE FROM responses WHERE question_id = ?";
            preparedStatement = connection.prepareStatement(deleteResponsesSQL);
            preparedStatement.setInt(1, questionId);
            preparedStatement.executeUpdate();

            preparedStatement.close();

            // Delete the question
            String deleteQuestionSQL = "DELETE FROM questions WHERE id = ?";
            preparedStatement = connection.prepareStatement(deleteQuestionSQL);
            preparedStatement.setInt(1, questionId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.closeConnections(connection, preparedStatement, null);
        }
    }

    public Question getQuestionById(int questionId) {
        Question question = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = dbManager.getConnection();

            // Get the question with the specified ID
            String querySQL = "SELECT q.id, q.content, q.difficulty_rank, t.id as topic_id, t.name as topic_name " +
                    "FROM questions q JOIN topics t ON q.topic_id = t.id WHERE q.id = ?";
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.setInt(1, questionId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Topic topic = new Topic();
                topic.setId(resultSet.getInt("topic_id"));
                topic.setName(resultSet.getString("topic_name"));

                question = new Question();
                question.setId(resultSet.getInt("id"));
                question.setContent(resultSet.getString("content"));
                question.setDifficultyRank(resultSet.getInt("difficulty_rank"));
                question.setTopic(topic);
            }

            resultSet.close();
            preparedStatement.close();

            // Get the responses for the question
            if (question != null) {
                String responseSQL = "SELECT id, text, is_correct FROM responses WHERE question_id = ?";
                preparedStatement = connection.prepareStatement(responseSQL);
                preparedStatement.setInt(1, question.getId());
                resultSet = preparedStatement.executeQuery();

                List<Response> responses = new ArrayList<>();
                while (resultSet.next()) {
                    Response response = new Response();
                    response.setId(resultSet.getInt("id"));
                    response.setText(resultSet.getString("text"));
                    response.setCorrect(resultSet.getBoolean("is_correct"));
                    responses.add(response);
                }
                question.setResponses(responses);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.closeConnections(connection, preparedStatement, resultSet);
        }

        return question;
    }

    public List<Question> searchQuestionByTopic(String topicName) {
        List<Question> questions = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = dbManager.getConnection();

            String searchSQL = "SELECT q.id, q.content, q.difficulty_rank, t.id as topic_id, t.name as topic_name " +
                    "FROM questions q JOIN topics t ON q.topic_id = t.id WHERE t.name = ?";
            preparedStatement = connection.prepareStatement(searchSQL);
            preparedStatement.setString(1, topicName);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Topic topic = new Topic();
                topic.setId(resultSet.getInt("topic_id"));
                topic.setName(resultSet.getString("topic_name"));

                Question question = new Question();
                question.setId(resultSet.getInt("id"));
                question.setContent(resultSet.getString("content"));
                question.setDifficultyRank(resultSet.getInt("difficulty_rank"));
                question.setTopic(topic);

                questions.add(question);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.closeConnections(connection, preparedStatement, resultSet);
        }

        return questions;
    }
}

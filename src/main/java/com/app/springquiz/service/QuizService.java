package com.app.springquiz.service;

import com.app.springquiz.model.Quiz;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuizService {

    private final Map<Integer, Quiz> questions = new HashMap<>();

    public Quiz getQuizById(int id){
        return questions.get(id);
    }

    public List<Quiz> loadQuizzes() {
        return List.copyOf(questions.values());
    }

    public void addQuiz(Quiz quiz) {
        questions.put(quiz.getId(), quiz);
    }

    public void editQuiz(Quiz quiz) {
        questions.put(quiz.getId(), quiz);
    }

    public void deleteQuiz(int id) {
        if (!questions.containsKey(id)) {
            throw new RuntimeException("Question id doesnt exists");
        }
        questions.remove(id);
    }

    public int getNextId(){
        return questions.size();
    }

}

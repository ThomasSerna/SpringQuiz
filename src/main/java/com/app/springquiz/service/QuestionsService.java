package com.app.springquiz.service;

import com.app.springquiz.model.Question;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionsService {

    private final Map<Integer, Question> questions = new HashMap<>();

    public List<Question> loadQuizzes() {
        return List.copyOf(questions.values());
    }

    public void addQuiz(Question question) {
        if (questions.containsKey(question.getId())) {
            throw new RuntimeException("Question id already exists");
        }
        questions.put(question.getId(), question);
    }

    public void editQuiz(Question question) {
        questions.put(question.getId(), question);
    }

    public void deleteQuiz(int id) {
        if (!questions.containsKey(id)) {
            throw new RuntimeException("Question id doesnt exists");
        }
        questions.remove(id);
    }


}

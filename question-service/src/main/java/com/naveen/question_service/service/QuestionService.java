package com.naveen.question_service.service;

import com.naveen.question_service.model.Question;
import com.naveen.question_service.model.QuestionWrapper;
import com.naveen.question_service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.naveen.question_service.dao.QuestionDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;
    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Question>> getQuestionByCategory(String category) {
        try {
            return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Optional<Question>> getQuestionById(Integer id) {
        try {
            return new ResponseEntity<>(questionDao.findById(id), HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Question> addQuestion(Question question) {
        try {
            return new ResponseEntity<>(questionDao.save(question),HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String category, Integer numQ) {
        List<Integer> questions = questionDao.findRandomQuestionsByCategory(category,numQ);
        return new ResponseEntity<>(questions,HttpStatus.OK);
    }


    public ResponseEntity<List<QuestionWrapper>> getQuestionFromId(List<Integer> questionId) {
        List<QuestionWrapper> questionWrappers  = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        for(Integer id : questionId){
            questions.add(questionDao.findById(id).get());
        }
        for(Question q : questions){
            QuestionWrapper q1 = new QuestionWrapper();
            q1.setId(q.getId());
            q1.setQuestionTitle(q.getQuestionTitle());
            q1.setOption1(q.getOption1());
            q1.setOption2(q.getOption2());
            q1.setOption3(q.getOption3());
            q1.setOption4(q.getOption4());
            questionWrappers.add(q1);
        }
        return new ResponseEntity<>(questionWrappers,HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateScore(List<Response> responses) {
        Integer ans = 0;
        for(Response r : responses){
            Question q = questionDao.findById(r.getId()).get();
            if(r.getResponse().equals(q.getRightAnswer()))
                ans++;
        }
        return new ResponseEntity<>(ans,HttpStatus.OK);
    }
}

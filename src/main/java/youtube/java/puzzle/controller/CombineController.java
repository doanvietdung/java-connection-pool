package youtube.java.puzzle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import youtube.java.puzzle.college.entity.College;
import youtube.java.puzzle.college.repository.CollegeRepository;
import youtube.java.puzzle.model.Response;
import youtube.java.puzzle.student.repository.StudentRepository;

import java.util.List;

@RestController
public class CombineController {

    @Autowired
    CollegeRepository collegeRepository;
    @Autowired
    StudentRepository studentRepository;

    @GetMapping(value = "/")
    public List<College> getResponse(){
        Response response = new Response();
        response.setStudents(studentRepository.findAll());
        response.setColleges(collegeRepository.findAll());
        return response.getColleges();
    }
}
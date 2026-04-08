package com.example.demo.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AddStudentDto;
import com.example.demo.dto.StudentDto;
import com.example.demo.service.StudentService;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;




@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/students")
@RestController
public class HelloController {
    private final StudentService studentService;
    public HelloController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<StudentDto> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping
    public void createNewStudent(@RequestBody AddStudentDto addStudentDto) {
        studentService.createNewStudent(addStudentDto);
    }

    @GetMapping("/{id}")
    public StudentDto getStudentByID(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }
    @GetMapping("/search")
    public List<StudentDto> getMethodName(@RequestParam String name) {
       return studentService.getStudentsByName(name);
    }
    @DeleteMapping("/{id}")
    public void deleteStudentById(@PathVariable Long id){
        studentService.deleteStudentById(id);
    }
    @PutMapping("/{id}")
    public StudentDto updateStudentDetails(@PathVariable Long id, @RequestBody AddStudentDto addStudentDto) {
        return studentService.updateStudentDetails(id, addStudentDto);
    }
}

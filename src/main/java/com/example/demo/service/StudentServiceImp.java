package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Repository.StudentRepository;
import com.example.demo.dto.AddStudentDto;
import com.example.demo.dto.StudentDto;
import com.example.demo.entity.Student;

@Service
public class StudentServiceImp implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImp(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public StudentDto getStudentById(Long id) {
        Student st = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No student with the id" + id));

        return new StudentDto(st.getId(), st.getName(), st.getEmail());
    }

    @Override
    public StudentDto createNewStudent(AddStudentDto addStudentDto) {
        Student student = new Student(addStudentDto.getName(), addStudentDto.getEmail());
        Student st = studentRepository.save(student);
        return new StudentDto(st.getId(), st.getName(), st.getEmail());

    }

    @Override
    public List<StudentDto> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        List<StudentDto> studentDtos = new ArrayList<>();
        for (Student st : students) {
            studentDtos.add(new StudentDto(st.getId(), st.getName(), st.getEmail()));
        }
        return studentDtos;
    }

    @Override
    public List<StudentDto> getStudentsByName(String name) {
        List<Student> students = studentRepository.findAllByName(name);
        List<StudentDto> studentDtos = new ArrayList<>();
        for (Student st : students) {
            studentDtos.add(new StudentDto(st.getId(), st.getName(), st.getEmail()));
        }
        return studentDtos;
    }

    @Override
    public StudentDto updateStudentDetails(Long id, AddStudentDto addStudentDto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No student with the id" + id));
        student.setEmail(addStudentDto.getEmail());
        student.setName(addStudentDto.getName());
        Student st = studentRepository.save(student);
        StudentDto studentDto = new StudentDto(st.getId(), st.getName(), st.getEmail());
        return studentDto;
    }

    @Override
    public void deleteStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No student found with id" + id));
        studentRepository.delete(student);
    }

}

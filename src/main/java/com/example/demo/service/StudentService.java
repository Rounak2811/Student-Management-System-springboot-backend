package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.AddStudentDto;
import com.example.demo.dto.StudentDto;

public interface StudentService {
    
    public List<StudentDto> getAllStudents();
    public StudentDto getStudentById(Long id);
    public List<StudentDto> getStudentsByName(String name);
    public StudentDto createNewStudent(AddStudentDto addStudentDto);
    public StudentDto updateStudentDetails(Long id,AddStudentDto addStudentDto);
    public void deleteStudentById(Long id);
}
